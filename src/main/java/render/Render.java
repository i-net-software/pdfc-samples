package render;

import com.inet.pdfc.PDFC;
import com.inet.pdfc.PDFComparer;
import com.inet.pdfc.generator.model.DiffGroup;
import com.inet.pdfc.generator.model.Modification;
import com.inet.pdfc.model.PagedElement;
import com.inet.pdfc.results.ResultModel;
import com.inet.pdfc.results.ResultPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * A sample to show the PDF render function with a simple function for displayer markers where differences are detected.
 * Expected 2 arguments, the path of the PDF files
 */
public class Render {

    private JFrame frame;

    /**
     * Start the sample, to show the PDF render function with a simple function for displayer markers
     * where differences are detected.
     *
     * @param args Expected 2 arguments, the path of the PDF files
     */
    public static void main( String[] args ) {
        try {
            PDFC.requestAndSetTrialLicenseIfRequired();
        } catch( IOException e ) {
            e.printStackTrace();
        }

        File[] files = getFileOfArguments( args );
        new Render( files ).show();
    }

    /**
     * For initialization.
     *
     * @param files Expected 2 PDF files
     */
    public Render( final File[] files ) {
        PDFComparer pdfComparer = new PDFComparer();
        ResultModel compare = pdfComparer.compare( files[0], files[1] );
        ResultPage page = compare.getPage( 0, true );

        frame = new JFrame();
        frame.setTitle( "PDF Difference" );
        frame.setSize( page.getWidth(), page.getHeight() );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        try {
            frame.add( new PDFViewer( compare ) );
        } catch( IOException e ) {
            e.printStackTrace();
        }
    }

    /**
     * For show the gui frame
     */
    public void show() {
        frame.setVisible( true );
    }

    /**
     * To show one PDF file.
     * Every click goes to the next page,
     * if no next page exists, it returns to page 1
     * Every change is marked with a blue transparent color
     * over the line.
     */
    public class PDFViewer extends JComponent {

        private int currentPageIndex = 0;
        private       ResultModel compare;
        private final int         maxPageNumber;

        /**
         * To show one PDF file.
         * Every click goes to the next page,
         * if no next page exists, it returns to page 1
         * Every change is marked with a blue transparent color
         * over the line.
         *
         * @param compare A comparer between 2 PDF files
         * @throws IOException
         */
        public PDFViewer( final ResultModel compare ) throws IOException {
            this.compare = compare;
            maxPageNumber = compare.getComparisonParameters().getFirstFile().getContent().getNumPages();
            addMouseListener( new MouseListener() {
                /**
                 * Every click goes to the next page,
                 * if no next page exists, it returns to page 1
                 * @param e mouse event
                 */
                @Override
                public void mouseClicked( MouseEvent e ) {
                    ++currentPageIndex;
                    if( maxPageNumber <= currentPageIndex ) {
                        currentPageIndex = 0;
                    }
                    repaint();
                }

                @Override
                public void mousePressed( MouseEvent e ) {
                }

                @Override
                public void mouseReleased( MouseEvent e ) {
                }

                @Override
                public void mouseEntered( MouseEvent e ) {
                }

                @Override
                public void mouseExited( MouseEvent e ) {
                }
            } );
        }

        @Override
        public void paint( Graphics g ) {
            super.paint( g );
            Graphics2D g2d = (Graphics2D)g;

            ResultPage page = compare.getPage( currentPageIndex, true );
            //draw the PDF file, alternative: page.renderPage(1.0, g2d  );
            BufferedImage pageImage = page.getPageImage( 1.0 );
            g2d.drawImage( pageImage, 0, 0, null );

            //for highlight the differences lines
            g2d.setColor( new Color( 0, 40, 255, 40 ) ); //transparent blue
            List<DiffGroup> differences = compare.getDifferences( false );
            for( DiffGroup difference : differences ) {
                if( hasChangesForThisPage( difference.getModifications() ) ) {
                    Rectangle bounds = difference.getBounds( true );
                    g2d.fillRect( bounds.x, bounds.y - currentPageIndex * page.getHeight(), page.getWidth(),
                                  bounds.height );
                }
            }
        }

        /**
         * Check the modifications found for the current page.
         *
         * @param modifications a list with modifications
         * @return true the modification is for the current page, false if it is no modification for the current page
         */
        private boolean hasChangesForThisPage( final List<Modification> modifications ) {
            if( modifications == null ) {
                return false;
            }

            for( Modification modification : modifications ) {
                List<PagedElement> affectedElements = modification.getAffectedElements( true );
                for( PagedElement affectedElement : affectedElements ) {
                    int pageIndex = affectedElement.getPageIndex();
                    if( pageIndex == currentPageIndex ) {
                        return true;
                    }
                }
                affectedElements = modification.getAffectedElements( false );
                for( PagedElement affectedElement : affectedElements ) {
                    int pageIndex = affectedElement.getPageIndex();
                    if( pageIndex == currentPageIndex ) {
                        return true;
                    }
                }
            }
            return false;
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
