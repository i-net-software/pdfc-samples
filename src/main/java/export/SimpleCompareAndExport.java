package export;

import com.inet.pdfc.PDFC;
import com.inet.pdfc.PDFComparer;
import com.inet.pdfc.error.PdfcException;
import com.inet.pdfc.presenter.DifferencesPDFPresenter;

import java.io.File;
import java.io.IOException;

/**
 * A simple sample for exporting the results of the comparison of 2 PDF Files to a PDF.
 * Expects 2 arguments - the paths of the PDF files to be compared
 */
public class SimpleCompareAndExport {

    /**
     * Start the sample, that show how exporting the result of a comparison of 2 PDF Files to a PDF,
     * showing how to set the export path for this comparison report.
     *
     * @param args Expects 2 arguments: the paths of the 2 PDF files that will be compared,
     */
    public static void main( String[] args ) {
        try {
            PDFC.requestAndSetTrialLicenseIfRequired();
        } catch( IOException e ) {
            e.printStackTrace();
        }

        File[] files = getFileOfArguments( args );

        //Used the current i-net PDFC configuration. If no configuration has been previously set then the default configuration will be used.
        DifferencesPDFPresenter differencesPDFPresenter = new DifferencesPDFPresenter( files[0].getParentFile() );
        try {
            new PDFComparer()
                            .addPresenter( differencesPDFPresenter )
                            .compare( files[0], files[1] );
        } catch( PdfcException e ) {
            e.printStackTrace();
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
     * @param file Path to the File
     * @return The Fileobject
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
