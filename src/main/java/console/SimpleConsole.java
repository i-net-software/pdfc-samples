package console;

import com.inet.pdfc.PDFComparer;
import com.inet.pdfc.error.PdfcException;
import com.inet.pdfc.presenter.ConsolePresenter;
import util.SampleUtil;

import java.io.File;

/**
 * A sample for logger output.
 *
 * Here it will be use the default
 */
public class SimpleConsole {

    public static void main( String[] args ) {
        File[] files = getFileOfArguments( args );
        PDFComparer pdfComparer = new PDFComparer().addPresenter( new ConsolePresenter() );
        try {
            pdfComparer.compare( files[1], files[0] );
            SampleUtil.showPresenterError( pdfComparer );
        } catch( PdfcException e ) {
            e.printStackTrace();
        }
    }

    /**
     * Get 2 Files back, that was checked
     *
     * @param args the arguments
     * @return 2 Files
     */
    public static File[] getFileOfArguments(final String[] args){
        if (args == null || args.length != 2) {
            throw new IllegalArgumentException( "Usage: CompareTwoFilesAndPrint <PDF-File1> <PDF-File2>" );
        }
        return new File[]{ SampleUtil.checkAndGetFile( args[0] ), SampleUtil.checkAndGetFile( args[1] )};
    }
}
