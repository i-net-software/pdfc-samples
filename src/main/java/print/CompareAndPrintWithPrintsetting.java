package print;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;

import com.inet.pdfc.PDFC;
import com.inet.pdfc.PDFComparer;
import com.inet.pdfc.config.FilePdfSource;
import com.inet.pdfc.presenter.DifferencesPrintPresenter;
import com.inet.pdfc.results.ResultModel;

/**
 * A sample for printing the result of the comparison of 2 PDF files
 * with some print settings
 * Expected 2 arguments, the path of the PDF files
 */
public class CompareAndPrintWithPrintsetting {

    /**
     * Start the sample, to show the printing function of the result of comparison between 2 PDF files
     * with some print settings
     *
     * @param args Expected 2 arguments, the path of the PDF files
     */
    public static void main( String[] args ) {
        File[] files = getFileOfArguments( args );

        //Used the current i-net PDFC configuration. If no configuration has been previously set then the default configuration will be used.

        //set up Printer service
        PrintService printService = PrintServiceLookup
                        .lookupDefaultPrintService(); //use the default printservice, for testing purpose it makes sense to use a virtual printer!
        PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
        //select paper A4
        attributes.add( MediaSizeName.ISO_A4 );
        //select orientation landscape
        attributes.add( OrientationRequested.PORTRAIT );

        CompletableFuture<ResultModel> resultModelCompletableFuture =
                        new PDFComparer().compareAsync( new FilePdfSource( files[0] ), new FilePdfSource( files[1] ) );

        try {
            ResultModel resultModel = resultModelCompletableFuture.get();
            new DifferencesPrintPresenter( printService, attributes ).executeImmediately( resultModel );
            resultModel.close();
        } catch( InterruptedException e ) {
            e.printStackTrace();
        } catch( ExecutionException e ) {
            e.printStackTrace();
        } catch( Exception e ) {
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
