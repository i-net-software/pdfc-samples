package export;

import com.inet.config.ConfigurationManager;
import com.inet.pdfc.PDFComparer;
import com.inet.pdfc.error.PdfcException;
import com.inet.pdfc.presenter.DifferencesPDFPresenter;
import util.SampleUtil;

import java.io.File;

/**
 * A simple Sample for export to pdf file the comparing between 2 PDF Files
 *
 * Expected 2 arguments, the path of the pdf files
 */
public class SimpleCompareAndExport{

    public static void main( String[] args ) {
        File[] files = getFileOfArguments( args );

        //Used the current i-net PDFC configuration. If no configuration has been previously set then the default configuration will be used.
        DifferencesPDFPresenter differencesPDFPresenter = new DifferencesPDFPresenter( files[0].getParentFile() );
        PDFComparer pdfComparer = new PDFComparer().addPresenter( differencesPDFPresenter );
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
        ConfigurationManager.getInstance().setCurrent( ConfigurationManager.getInstance().get( 1, "Default" ) );
        if (args == null || args.length != 2) {
            throw new IllegalArgumentException( "Usage: CompareTwoFilesAndPrint <PDF-File1> <PDF-File2>" );
        }
        return new File[]{ SampleUtil.checkAndGetFile( args[0] ), SampleUtil.checkAndGetFile( args[1] )};
    }
}
