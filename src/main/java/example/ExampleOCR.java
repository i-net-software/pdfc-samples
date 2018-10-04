package example;

import com.inet.pdfc.PDFComparer;
import com.inet.pdfc.config.*;
import com.inet.pdfc.error.PdfcException;
import com.inet.pdfc.presenter.DifferencesPDFPresenter;
import util.SampleUtil;

import java.io.File;
import java.util.InvalidPropertiesFormatException;

/**
 * Example for OCR
 *
 * How do work with a profile file
 * Work with an other parser (multiple image file)
 * Work with OCR
 */
public class ExampleOCR {

    /**
     * Easy example with own profile and setting usage
     * @param args a output directory
     */
    public static void main( String[] args ) {
        SampleUtil.filterServerPlugins();
        ExampleOCR exampleOCR = new ExampleOCR();
        exampleOCR.startCompare( getFileOfArguments( args ) );
    }

    private IProfile comparisonSettings;

    /**
     * Initialize the sample variables
     */
    public ExampleOCR() {
        try {
            comparisonSettings = new XMLProfile( new File( getClass().getResource( "/Profile_Custom_OCR.xml" ).getFile() ) );
        } catch( InvalidPropertiesFormatException e ) {
            e.printStackTrace();
        }
    }

    /**
     * Get 2 Files back, that was checked
     * @param args the arguments
     * @return 2 Files
     */
    public static File getFileOfArguments( final String[] args ) {
        if( args == null || args.length != 1 ) {
            throw new IllegalArgumentException( "Usage: ExampleOCR <PDF-Directory-Output>" );
        }
        if( args[0] == null ) {
            throw new IllegalArgumentException( "The parameter is empty.\n parameter = " + args[0] );
        }
        final File fileObject = new File( args[0] );

        if( !fileObject.exists() ) {
            throw new IllegalArgumentException( "The file didn't exist.\n parameter = " + args[0] );
        }
        if( !fileObject.isDirectory() ) {
            throw new IllegalArgumentException( "The file is not a folder.\n parameter = " + fileObject );
        }
        return fileObject;
    }

    /**
     * Start the comparison with the "i-net_PDFC_-_Command_Line_Access document"
     * @param fileDirectory output directory
     */
    public void startCompare( final File fileDirectory ) {
        //Used the current i-net PDFC configuration. If no configuration has been previously set then the default configuration will be used.
        DifferencesPDFPresenter differencesPDFPresenter = new DifferencesPDFPresenter( fileDirectory );
        PDFComparer pdfComparer = new PDFComparer();
        if( comparisonSettings != null ) {
            pdfComparer.setProfile( comparisonSettings );
        }
        pdfComparer.addPresenter( differencesPDFPresenter );

        try {
            File file1 = new File( getClass().getResource( "/Optical-Character-Recognition.pdf" ).getFile() );
            File file2 = new File( getClass().getResource( "/Optical-Character-Recognition.zip" ).getFile() );

            pdfComparer.compare( file1, file2 );
            SampleUtil.showPresenterError( pdfComparer );
        } catch( PdfcException e ) {
            e.printStackTrace();
        }
    }
}
