package configuration;

import com.inet.config.ConfigurationManager;
import com.inet.pdfc.PDFComparer;
import com.inet.pdfc.config.*;
import com.inet.pdfc.generator.model.DiffGroup;
import com.inet.pdfc.generator.model.Modification;
import com.inet.pdfc.results.ResultModel;

import java.io.File;
import java.util.List;

/**
 * A simple sample for using regex.
 *
 * Expected 2 arguments, the path of the pdf files
 */
public class UseRegex {

    public static void main( String[] args ) {
        File[] files = getFileOfArguments( args );

        PDFComparer pdfComparer = new PDFComparer();

        System.out.println( "\nFiltered "  );
        IConfiguration configuration = new DefaultConfiguration(  );

        //remove all number text changes (for example page number)
        configuration.putValue( PDFCProperty.FILTER_PATTERNS,
                        "[0-9]|regexp|active\n" );
        configuration.putValue( PDFCProperty.CONTINUOUS_FILTERS, "REGEXP" );

        showModification( pdfComparer
                                          .setConfiguration( configuration )
                                          .compare( files[0], files[1] ) );
    }

    public static void showModification(final ResultModel result){
        List<DiffGroup> differences = result.getDifferences( false );

        for( DiffGroup difference : differences ) {
            List<Modification> modifications = difference.getModifications();
            if(modifications != null) {
                for( Modification modification : modifications ) {
                    System.out.println( "modification = " + modification );
                    modification.getModificationType();
                }
            }
        }
    }

    /**
     * Get 2 Files back, that was checked
     *
     * @param args the arguments
     * @return 2 Files
     */
    public static File[] getFileOfArguments(final String[] args){
        ConfigurationManager.getInstance().setCurrent( ConfigurationManager.getInstance().get( 1, "Default" ) );
        if (args == null || args.length != 2) {
            throw new IllegalArgumentException( "Usage: CompareTwoFilesAndPrint <PDF-File1> <PDF-File2>" );
        }
        return new File[]{ checkAndGetFile( args[0] ), checkAndGetFile( args[1] )};
    }

    /**
     * For get a File-Object out a String-Path
     *
     * Check for null, exists and directory
     *
     * @param file Path for the File
     * @return The Fileobject
     */
    public static File checkAndGetFile( final String file){
        if(file == null){
            throw new IllegalArgumentException( "The parameter is empty.\n parameter = " + file );
        }
        final File fileObject = new File( file );

        if( ! fileObject.exists() ){
            throw new IllegalArgumentException( "The file didn't exist.\n parameter = " + file );
        }
        if( fileObject.isDirectory()){
            throw new IllegalArgumentException( "The file is a folder and not a pdf file.\n parameter = " + file );
        }

        return  fileObject;
    }


}
