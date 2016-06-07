package differences;

import com.inet.pdfc.PDFC;
import com.inet.pdfc.PDFComparer;
import com.inet.pdfc.generator.message.InfoData;
import com.inet.pdfc.results.ResultModel;

import java.io.File;
import java.io.IOException;

/**
 * A sample to show the difference number of pages between 2 pdf files.
 *
 * Expected 2 arguments, the path of the pdf files
 */
public class NumOfDifferencePageNumber{

    public static void main( String[] args ) {
        try {
            PDFC.requestAndSetTrialLicenseIfRequired();
        } catch( IOException e ) {
            e.printStackTrace();
        }

        File[] files = getFileOfArguments( args );
        PDFComparer pdfComparer = new PDFComparer();

        ResultModel result = new PDFComparer().compare( files[0], files[1] );
        InfoData infoData = result.getComparisonParameters();

        int firstTotalPageNumber = infoData.getFirstTotalPageNumber();
        int secondTotalPageNumber = infoData.getSecondTotalPageNumber();
        System.out.println( "firstTotalPageNumber = " + firstTotalPageNumber );
        System.out.println( "secondTotalPageNumber = " + secondTotalPageNumber );

        int differencePageNumber = firstTotalPageNumber - secondTotalPageNumber;

        System.out.println( "difference page number = " + differencePageNumber );
    }

    /**
     * Get 2 files back that are to be checked for comparisons
     *
     * @param args the arguments
     * @return 2 files to compare
     */
    public static File[] getFileOfArguments(final String[] args){
        if (args == null || args.length != 2) {
            throw new IllegalArgumentException( "Usage: CompareTwoFilesAndPrint <PDF-File1> <PDF-File2>" );
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
}
