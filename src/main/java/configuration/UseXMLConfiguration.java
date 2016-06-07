package configuration;

import com.inet.pdfc.PDFC;
import com.inet.pdfc.PDFComparer;
import com.inet.pdfc.config.*;
import com.inet.pdfc.presenter.DifferencesPrintPresenter;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.io.File;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;

/**
 * A sample to show, how use a PDFC-Config XML-File.
 *
 * Expected 3 arguments, the first 2 arguments for the path of the PDF files and the last one
 * for the XML config file.
 */
public class UseXMLConfiguration {

    // TODO : Javadoc
    public static void main( String[] args ) {
        try {
            PDFC.requestAndSetTrialLicenseIfRequired();
        } catch( IOException e ) {
            e.printStackTrace();
        }

        File[] files = getFileOfArguments( args );
        IConfiguration configuration = null;
        try {
            configuration = new XMLConfiguration( files[2] );
        } catch( InvalidPropertiesFormatException e ) {
            System.out.println("The file = " + files[2] + " is not a correct XML-Configuration File" );
            e.printStackTrace();
        }

        PrintService printService = PrintServiceLookup.lookupDefaultPrintService(); //use the default printservice, for testing purpose it makes sense to use a virtual printer!
        PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();

        new PDFComparer()
                        .setConfiguration( configuration )
                        .addPresenter( new DifferencesPrintPresenter( printService, attributes ) )
                        .compare( files[1], files[0] );

    }


    /**
     * Get 2 files that are to be checked for comparisons
     *
     * @param args the arguments
     * @return 2 files to compare
     */
    public static File[] getFileOfArguments( final String[] args ) {
        if( args == null || args.length != 3 ) {
            throw new IllegalArgumentException( "Usage: CompareTwoFilesAndPrint <PDF-File1> <PDF-File2> <XML-Configuration-File>" );
        }
        return new File[] { checkAndGetFile( args[0] ), checkAndGetFile( args[1]), checkAndGetFile( args[2] )};
    }

    /**
     * Returns a File object based on a string path
     *
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
