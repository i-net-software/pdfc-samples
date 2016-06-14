package junit;

import com.inet.pdfc.PDFC;
import com.inet.pdfc.PDFComparer;
import com.inet.pdfc.generator.message.InfoData;
import com.inet.pdfc.results.ResultModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * A sample for writing JUnit test cases using PDFC
 */
public class CompareTest {

    private PDFComparer pdfComparer;

    @Before
    public void before() {
        try {
            PDFC.requestAndSetTrialLicenseIfRequired();
        } catch( IOException e ) {
            e.printStackTrace();
        }

        pdfComparer = new PDFComparer();
    }

    @Test
    public void testDifferences() {

        File example1 = new File( getClass().getResource( "/Example1.pdf" ).getFile() );
        File example2 = new File( getClass().getResource( "/Example2.pdf" ).getFile() );

        ResultModel result = pdfComparer.compare( example1, example2 );
        InfoData comparisonParameters = result.getComparisonParameters();

        Assert.assertEquals( 10, result.getDifferencesCount( false ) );
        Assert.assertEquals( 10, result.getDifferencesCount( true ) );

        Assert.assertEquals( 3, comparisonParameters.getFirstTotalPageNumber() );
        Assert.assertEquals( 3, comparisonParameters.getSecondTotalPageNumber() );
    }
}
