package differences;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.inet.pdfc.PDFC;
import com.inet.pdfc.PDFComparer;
import com.inet.pdfc.error.PdfcException;
import com.inet.pdfc.generator.model.DiffGroup;
import com.inet.pdfc.generator.model.DifferencePages;
import com.inet.pdfc.generator.model.Modification;
import com.inet.pdfc.results.ResultModel;

/**
 * A sample that calculates the number of differences between 2 PDF files, grouped by page.
 * Expects 2 arguments - the paths of the PDF files
 */
public class NumOfDifferencePerPage {

    /**
     * Start the sample, that calculates the number of differences between 2 PDF files, grouped by page.
     *
     * @param args Expected 2 arguments, the path of the PDF files
     */
    public static void main( String[] args ) {
        try {
            PDFC.requestAndSetTrialLicenseIfRequired();
        } catch( IOException e ) {
            e.printStackTrace();
        }

        File[] files = getFileOfArguments( args );
        PDFComparer pdfComparer = new PDFComparer();
        ResultModel result = null;
        try {
            result = pdfComparer.compare( files[0], files[1] );

            //Array for the result
            int[] changePerPage = new int[Math.max( result.getPageCount( true ), result.getPageCount( false ) )];

            List<DiffGroup> differences = result.getDifferences( false );
            for( DiffGroup difference : differences ) {
                List<Modification> modifications = difference.getModifications();
                if( modifications != null ) {
                    for( Modification modification : modifications ) {
                        DifferencePages differencePages = modification.getDifferencePages( true );
                        if( differencePages != null ) {
                            int startDiff = differencePages.getStartPage();
                            int endDiff = differencePages.getEndPage();

                            for( ; startDiff <= endDiff; ++startDiff ) {
                                changePerPage[startDiff - 1] = changePerPage[startDiff - 1] + 1;
                            }
                        }
                    }
                }
            }

            //output the result
            for( int i = 0; i < changePerPage.length; i++ ) {
                System.out.println( (i + 1) + ". page from first document has change = " + changePerPage[i] );
            }
        } catch( PdfcException e ) {
            e.printStackTrace();
        } finally {
            if( result != null ) {
                result.close();
            }
        }
    }

    /**
     * Get 2 files that are to be checked for comparisons
     *
     * @param args the arguments
     * @return 2 files to compare
     */
    public static File[] getFileOfArguments( final String[] args ) {
        if( args == null || args.length != 2 ) {
            throw new IllegalArgumentException( "Usage: CompareTwoFilesAndPrint <PDF-File1> <PDF-File2>" );
        }
        return new File[] { checkAndGetFile( args[0] ), checkAndGetFile( args[1] ) };
    }

    /**
     * Returns a File object based on a string path
     * The file must not be null, must exist and must not be a directory
     *
     * @param file path to the file
     * @return The File object
     */
    public static File checkAndGetFile( final String file ) {
        if( file == null ) {
            throw new IllegalArgumentException( "The parameter is empty.\n parameter = " + file );
        }
        final File fileObject = new File( file );

        if( !fileObject.exists() ) {
            throw new IllegalArgumentException( "The file didn't exist.\n parameter = " + file );
        }
        if( fileObject.isDirectory() ) {
            throw new IllegalArgumentException( "The file is a folder and not a PDF file.\n parameter = " + file );
        }

        return fileObject;
    }
}
