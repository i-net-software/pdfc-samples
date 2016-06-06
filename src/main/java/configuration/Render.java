package configuration;

import com.inet.config.ConfigurationManager;
import com.inet.pdfc.PDFComparer;
import com.inet.pdfc.generator.model.DiffGroup;
import com.inet.pdfc.results.ResultModel;
import com.inet.pdfc.results.ResultPage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

/**
 * A sample to show the render function with a simplify marker function.
 *
 * Expected 2 arguments, the path of the pdf files
 */
public class Render {

    public static void main( String[] args ) {
        File[] files = getFileOfArguments( args );

        PDFComparer pdfComparer = new PDFComparer();
        ResultModel compare = pdfComparer.compare( files[0], files[1] );
        ResultPage page = compare.getPage( 0, true );

        JFrame frame = new JFrame(  );
        frame.setTitle("PDF Difference");
        frame.setSize(page.getWidth(), page.getHeight());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add( new JComponent() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Graphics2D g2d = (Graphics2D) g;
                int pageNumber = 1;

                //draw the pdf file, alternative: page.renderPage(1.0, g2d  );
                BufferedImage pageImage = page.getPageImage( pageNumber );
                g2d.drawImage( pageImage, 0,0, null );



                //for highlight the differences lines
                g2d.setColor(new Color( 0,40,255,40  ) ); //transparent blue
                List<DiffGroup> differences = compare.getDifferences( false );
                for( DiffGroup difference : differences ) {
                    System.out.println( "difference = " + difference );

                    Rectangle bounds = difference.getBounds( true );
                    g2d.fillRect( bounds.x, bounds.y, page.getWidth(), bounds.height );
                }
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
