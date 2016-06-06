package junit;

import com.inet.config.ConfigurationManager;
import com.inet.pdfc.PDFComparer;
import com.inet.pdfc.generator.message.InfoData;
import com.inet.pdfc.results.ResultModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * A sample for JUnit-testcases
 */
public class CompareTest {

    private PDFComparer pdfComparer;

    @Before
    public void before(){
        ConfigurationManager.getInstance().setCurrent( ConfigurationManager.getInstance().get( 1, "Default" ) );
        pdfComparer = new PDFComparer();
    }

    @Test
    public void testDifference(){

        File example1 =  new File(getClass().getResource( "Example1.pdf" ).getFile());
        File example2 =  new File(getClass().getResource( "Example2.pdf" ).getFile());

        ResultModel result = pdfComparer.compare( example1, example2 );
        InfoData comparisonParameters = result.getComparisonParameters();

        Assert.assertEquals( 10, result.getDifferencesCount( false ));
        Assert.assertEquals( 10, result.getDifferencesCount( true ));

        Assert.assertEquals( 3, comparisonParameters.getFirstTotalPageNumber());
        Assert.assertEquals( 3, comparisonParameters.getSecondTotalPageNumber());
    }

}
