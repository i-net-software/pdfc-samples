package configuration;

import com.inet.config.ConfigurationManager;
import com.inet.pdfc.PDFComparer;
import com.inet.pdfc.config.*;
import com.inet.pdfc.presenter.DifferencesPDFPresenter;

import java.io.File;
import java.util.InvalidPropertiesFormatException;

/**
 * A sample to show, how use a PDFC-Config XML-File.
 *
 * Expected 3 arguments, the first 2 arguments for the path of the pdf files and the last one
 * for the xml config file.
 */
public class UseXMLConfiguration {

    public static void main( String[] args ) {
        File[] files = getFileOfArguments( args );
        IConfiguration configuration = null;
        try {
            configuration = new XMLConfiguration( files[2] );
        } catch( InvalidPropertiesFormatException e ) {
            System.out.println("The file = " + files[2] + " is not a correct XML-Configuration File" );
            e.printStackTrace();
        }

        DifferencesPDFPresenter differencesPDFPresenter = new DifferencesPDFPresenter( files[0].getParentFile() );
        new PDFComparer()
                        .setConfiguration( configuration )
                        .addPresenter( differencesPDFPresenter )
                        .compare( files[1], files[0] );
    }


    /**
     * Get 2 Files back, that was checked
     *
     * @param args the arguments
     * @return 2 Files
     */
    public static File[] getFileOfArguments( final String[] args ) {
        ConfigurationManager.getInstance().setCurrent( ConfigurationManager.getInstance().get( 1, "Default" ) );
        if( args == null || args.length != 3 ) {
            throw new IllegalArgumentException( "Usage: CompareTwoFilesAndPrint <PDF-File1> <PDF-File2> <XML-Configuration-File>" );
        }
        return new File[] { checkAndGetFile( args[0] ), checkAndGetFile( args[1]), checkAndGetFile( args[2] )};
    }

    /**
     * For get a File-Object out a String-Path
     * Check for null, exists and directory
     *
     * @param file Path for the File
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
            throw new IllegalArgumentException( "The file is a folder and not a pdf file.\n parameter = " + file );
        }

        return fileObject;
    }
}
