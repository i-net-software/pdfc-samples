package report;

import com.inet.config.ConfigurationManager;
import com.inet.pdfc.PDFComparer;
import com.inet.pdfc.presenter.ReportPDFPresenter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A sample for export to pdf file the comparing between 2 PDF Files,
 * for the case to change the export path.
 *
 * Expected 3 arguments, the path of the 2 pdf files that will be compared.
 * At least arguments the path for the export file. If no export file exist,
 * it will be create a new file.
 *
 * Similar to CompareAndExportToSpecificFilename
 */
public class ReportingToSpecificFilename {

    public static void main( String[] args ) {
        File[] files = getFileOfArguments( args );
        File exportFile = checkAndCreateFile( args[2] );

        //Used the current i-net PDFC configuration. If no configuration has been previously set then the default configuration will be used.
        ReportPDFPresenter reportPDFPresenter = new PersonalReportPDFPresenter( false, false, exportFile );


        new PDFComparer()
                        .addPresenter( reportPDFPresenter )
                        .compare( files[1], files[0] );
    }


    public static class PersonalReportPDFPresenter extends ReportPDFPresenter{

        /**
         * Export File
         */
        private File exportFile  = null;

        public PersonalReportPDFPresenter( boolean detailed, boolean appendSettings, File export ) {
            super( detailed, appendSettings, null );
            exportFile =  export;
        }

        /**
         * The import methode, to change the path of the export file
         * @return stream for export file
         * @throws IOException
         */
        @Override
        protected OutputStream getExportStream() throws IOException {
            if(exportFile == null) {
                return super.getExportStream();
            }else{
                return new FileOutputStream(exportFile);
            }
        }
    }

    /**
     * Get 2 files back that are to be checked for comparisons
     *
     * @param args the arguments
     * @return 2 files to compare
     */
    public static File[] getFileOfArguments(final String[] args){
        ConfigurationManager.getInstance().setCurrent( ConfigurationManager.getInstance().get( 1, "Default" ) );
        if (args == null || args.length != 3  ) {
            throw new IllegalArgumentException( "Usage: CompareTwoFilesAndPrint <PDF-File1> <PDF-File2> <PDF-File-Output>" );
        }
        return new File[]{ checkAndGetFile( args[0] ), checkAndGetFile( args[1] )};
    }

    /**
     * Returns a File object based on a string path
     *
     * The file must not be null, must exist and must not be a directory
     *
     * @param file Path to the File
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

    public static File checkAndCreateFile( final String file){
        final File fileObject = new File( file );

        try {
            fileObject.createNewFile();
        } catch( IOException e ) {
            e.printStackTrace();
            throw new IllegalArgumentException( "the export file can not will create" );
        }

        if( fileObject.isDirectory()){
            throw new IllegalArgumentException( "The file is a folder and not a pdf file.\n parameter = " + file );
        }

        return  fileObject;
    }
}
