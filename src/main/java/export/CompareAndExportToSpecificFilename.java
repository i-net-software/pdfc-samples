package export;

import com.inet.pdfc.PDFC;
import com.inet.pdfc.PDFComparer;
import com.inet.pdfc.presenter.DifferencesPDFPresenter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A sample for exporting the result of a comparison of 2 PDF Files to a PDF,
 * showing how to set the export path for this comparison report.
 * Expects 3 arguments: the paths of the 2 PDF files that will be compared,
 * and the path for the export file. If no export file exists,
 * a new file will be created.
 * See the ReportingToSpecificFilename sample for an additional sample along these lines.
 */
public class CompareAndExportToSpecificFilename {

    /**
     * Start the sample, that show how exporting the result of a comparison of 2 PDF Files to a PDF,
     * showing how to set the export path for this comparison report.
     *
     * @param args Expects 3 arguments: the paths of the 2 PDF files that will be compared,
     *             and the path for the export file. If no export file exists,
     *             a new file will be created.
     */
    public static void main( String[] args ) {
        try {
            PDFC.requestAndSetTrialLicenseIfRequired();
        } catch( IOException e ) {
            e.printStackTrace();
        }

        File[] files = getFileOfArguments( args );
        File exportFile = checkAndCreateFile( args[2] );

        //Used the current i-net PDFC configuration. If no configuration has been previously set then the default configuration will be used.
        DifferencesPDFPresenter differencesPDFPresenter = new PersonalDifferencesPDFPresenter( exportFile );

        new PDFComparer()
                        .addPresenter( differencesPDFPresenter )
                        .compare( files[1], files[0] );
    }

    /**
     * A help class, to specify the location of the output file.
     */
    public static class PersonalDifferencesPDFPresenter extends DifferencesPDFPresenter {

        private File exportFile = null;

        /**
         * A help class, to specify the location of the output file.
         *
         * @param export output file location
         */
        public PersonalDifferencesPDFPresenter( File export ) {
            super( null );
            exportFile = export;
        }

        /**
         * The import methode, to change the path of the export file
         *
         * @return stream for export file
         * @throws IOException
         */
        @Override
        protected OutputStream getExportStream() throws IOException {
            if( exportFile == null ) {
                return super.getExportStream();
            } else {
                return new FileOutputStream( exportFile );
            }
        }
    }

    /**
     * Get 2 files back that are to be checked for comparisons
     *
     * @param args the arguments
     * @return 2 files to compare
     */
    public static File[] getFileOfArguments( final String[] args ) {
        if( args == null || args.length != 3 ) {
            throw new IllegalArgumentException(
                            "Usage: CompareTwoFilesAndPrint <PDF-File1> <PDF-File2> <PDF-File-Output>" );
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

    /**
     * Returns a File object based on a string path
     * The file must not be null and must not be a directory
     * If the file not exist, it will be created
     *
     * @param file location to the file
     * @return The File object
     */
    public static File checkAndCreateFile( final String file ) {
        final File fileObject = new File( file );

        try {
            fileObject.createNewFile();
        } catch( IOException e ) {
            e.printStackTrace();
            throw new IllegalArgumentException( "the export file can not will create" );
        }

        if( fileObject.isDirectory() ) {
            throw new IllegalArgumentException( "The file is a folder and not a PDF file.\n parameter = " + file );
        }

        return fileObject;
    }
}
