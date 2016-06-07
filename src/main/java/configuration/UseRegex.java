package configuration;

import com.inet.pdfc.PDFC;
import com.inet.pdfc.PDFComparer;
import com.inet.pdfc.config.*;
import com.inet.pdfc.generator.model.DiffGroup;
import com.inet.pdfc.generator.model.Modification;
import com.inet.pdfc.results.ResultModel;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * A simple sample for using regular expressions for filtering which texts are to be compared.
 * Expects 2 arguments - the paths of the PDF files
 */
public class UseRegex {

    /**
     * Start the sample, that show how using regular expressions for filtering which texts are to be compared.
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

        System.out.println( "\nFiltered " );
        IConfiguration configuration = new DefaultConfiguration();

        configuration.putValue( PDFCProperty.FILTER_PATTERNS, ""
                        //for removing all numbers that are not in a text
                        + "\\s\\d+$|regexp|active\n"
                        + "^\\d+\\s|regexp|active\n"
                        + "\\s\\d+\\s|regexp|active\n"
                        + "^\\d+$|regexp|active\n"
                        //filtered date in format YYYY mm dd and dd mm YYYY
                        + "((19|20)\\d\\d([- /.])(0[1-9]|1[012])([- /.])(0[1-9]|[12][0-9]|3[01]))|regexp|active\n"
                        + "((0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d)|regexp|active\n"
                        //filtered length unit
                        + "\\s(mm|cm|dm|m|km)|regexp|active\n"
        );
        configuration.putValue( PDFCProperty.CONTINUOUS_FILTERS, "REGEXP" );

        showModifications( pdfComparer
                                           .setConfiguration( configuration )
                                           .compare( files[0], files[1] ) );
    }

    /**
     * Show all modifications
     *
     * @param result the result of the comparision of 2 PDF files
     */
    public static void showModifications( final ResultModel result ) {
        List<DiffGroup> differences = result.getDifferences( false );
        for( DiffGroup difference : differences ) {
            List<Modification> modifications = difference.getModifications();
            if( modifications != null ) {
                for( Modification modification : modifications ) {
                    System.out.println( "modification = " + modification );
                    modification.getModificationType();
                }
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
