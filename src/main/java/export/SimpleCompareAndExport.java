package export;

import com.inet.config.ConfigurationManager;
import com.inet.pdfc.PDFComparer;
import com.inet.pdfc.presenter.DifferencesPDFPresenter;

import java.io.File;

/**
 * A simple OutputSpecifyModifyTyps for export to pdf file the comparing between 2 PDF Files
 *
 * Expected 2 arguments, the path of the pdf files
 */
public class SimpleCompareAndExport{

    public static void main( String[] args ) {
        File[] files = getFileOfArguments( args );

        //Used the current i-net PDFC configuration. If no configuration has been previously set then the default configuration will be used.
        DifferencesPDFPresenter differencesPDFPresenter = new DifferencesPDFPresenter( files[0].getParentFile() );
        new PDFComparer()
                        .addPresenter( differencesPDFPresenter )
                        .compare( files[1], files[0] );
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
