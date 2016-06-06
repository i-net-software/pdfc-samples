package configuration;

import com.inet.cache.image.SerializableImage;
import com.inet.config.ConfigurationManager;
import com.inet.pdfc.PDFComparer;
import com.inet.pdfc.config.ConfigurationFactory;
import com.inet.pdfc.config.FilterType;
import com.inet.pdfc.config.IConfiguration;
import com.inet.pdfc.config.PDFCProperty;
import com.inet.pdfc.generator.message.HighlightData;
import com.inet.pdfc.generator.model.DiffGroup;
import com.inet.pdfc.generator.model.IDiffGroupBounds;
import com.inet.pdfc.generator.model.Modification;
import com.inet.pdfc.generator.model.diff.AttributeDifference;
import com.inet.pdfc.generator.rendercache.PageImageCache;
import com.inet.pdfc.generator.rendercache.SessionCacheStore;
import com.inet.pdfc.model.Document;
import com.inet.pdfc.model.Page;
import com.inet.pdfc.model.PagedElement;
import com.inet.pdfc.presenter.DifferencesPDFPresenter;
import com.inet.pdfc.results.ResultModel;
import com.inet.pdfc.results.ResultPage;
import com.inet.pdfc.thread.PdfcSession;
import com.inet.pdfc.thread.SessionCache;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by richardr on 03.06.2016.
 */
public class Render {

    public static void main( String[] args ) {
        File[] files = getFileOfArguments( args );

        PDFComparer pdfComparer = new PDFComparer();
        ResultModel compare = pdfComparer.compare( files[0], files[1] );
        ResultPage page = compare.getPage( 0, true );

        try {
            Document content = compare.getComparisonParameters().getFirstFile().getContent();
//            content.
            Page page1 = content.getPage( 0 );
//            page1.
        } catch( IOException e ) {
            e.printStackTrace();
        }

        PageImageCache pageImageCache = compare.getPageImageCache();
        int pageCount = compare.getMaxPageCount();

//        PdfcSession session = PdfcSession.getSession();
//        SessionCache sessionCache = session.getSessionCache();
//        SerializableImage image = sessionCache.getImage( 0 );

        compare.setHighlightVisibile( FilterType.INVISIBLEELEMENTS, true );

        JFrame frame = new JFrame(  );
        frame.setTitle("PDF");
        frame.setSize(page.getWidth(), page.getHeight());
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.add( new TestComponent( compare ) );
        frame.add( new JComponent() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Graphics2D g2d = (Graphics2D) g;

//                for( int i = 0; i < pageCount; i++ ) {
//                    if(pageImageCache.hasPage( false, i )) {
//                        compare.getPageImageCache().renderPage( false, 0, 1.0, g2d );

                List<HighlightData.Highlight> highlightsForPage = compare.getHighlightsForPage( 0, false );
                if(highlightsForPage != null) {
                    for( HighlightData.Highlight highlight : highlightsForPage ) {
                        System.out.println( "highlight = " + highlight );
                        //                        highlight.
                        Image image = highlight.getImage();
                        g2d.drawImage( image, highlight.x, highlight.y, null );

                        g2d.drawString( "NEUER TEXT" , 20,20 );
                    }
                }

                page.renderPage(  1.0, g2d);

//                List<DiffGroup> differences = compare.getDifferences( false );
//                for( DiffGroup difference : differences ) {
//                    List<PagedElement> addedElements = difference.getAddedElements();
//                    for( PagedElement addedElement : addedElements ) {
////                        addedElement.
//                    }
//
//                    List<Modification> modifications = difference.getModifications();
//                    for( Modification modification : modifications ) {
//                        List<AttributeDifference<?>> attributeDifferences = modification.getAttributeDifferences();
//                        for( AttributeDifference<?> attributeDifference : attributeDifferences ) {
////                            attributeDifference.
//                        }
//                    }
//
//                    IDiffGroupBounds boundingElements = difference.getBoundingElements();
////                    boundingElements.
//                }

                //                    }
//
//                }

            }
        } );
        frame.setVisible( true );

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
        return new File[]{ checkAndGetFile( args[0] ), checkAndGetFile( args[1] )};
    }

    /**
     * For get a File-Object out a String-Path
     *
     * Check for null, exists and directory
     *
     * @param file Path for the File
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
