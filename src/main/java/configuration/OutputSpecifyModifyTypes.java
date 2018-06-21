package configuration;

import com.inet.pdfc.PDFComparer;
import com.inet.pdfc.config.*;
import com.inet.pdfc.error.PdfcException;
import util.SampleUtil;

import java.io.File;

/**
 * A sample to show the modifications between 2 PDF files in a type-sorted list.
 * Expects 2 arguments - the paths of the PDF files
 */
public class OutputSpecifyModifyTypes {

    /**
     * Start the sample, that show the modifications between 2 PDF files in a type-sorted list.
     *
     * @param args Expected 2 arguments, the path of the PDF files
     */
    public static void main( String[] args ) {
        File[] files = getFileOfArguments( args );
        PDFComparer pdfComparer = new PDFComparer();
        IProfile profile = new DefaultProfile();
        try {
            System.out.println( "all modified texts" );
            profile.putValue( PDFCProperty.COMPARE_TYPES, "" + CompareType.TEXT );
            SampleUtil.showModifications( pdfComparer.setProfile( profile ).compare( files[0], files[1] ) );

            System.out.println( "\nall modified lines" );
            profile.putValue( PDFCProperty.COMPARE_TYPES, "" + CompareType.LINE );
            SampleUtil.showModifications( pdfComparer.setProfile( profile ).compare( files[0], files[1] ) );

            System.out.println( "\nall modified images" );
            profile.putValue( PDFCProperty.COMPARE_TYPES, "" + CompareType.IMAGE );
            SampleUtil.showModifications( pdfComparer.setProfile( profile ).compare( files[0], files[1] ) );
        }catch( PdfcException e ) {
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
        return new File[] { SampleUtil.checkAndGetFile( args[0] ), SampleUtil.checkAndGetFile( args[1] ) };
    }
}
