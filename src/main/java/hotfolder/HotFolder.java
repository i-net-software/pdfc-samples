package hotfolder;

import com.inet.config.ConfigurationManager;
import com.inet.pdfc.PDFComparer;
import com.inet.pdfc.presenter.DifferencesPDFPresenter;
import com.inet.pdfc.results.ResultModel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A Java app which watches a folder for file changes and automatically compares new PDFs.
 * The idea is that any time two PDFs are added to the folder, we will run a simple comparison of the two
 * PDFs using i-net PDFC, create a report of the comparison and store it in a "reports" folder, and move
 * the source PDFs to a source archive folder.
 * See https://faq.inetsoftware.de/t/automatically-compare-new-pdfs-in-a-folder/47
 */
public class HotFolder {
    
    private static Path firstPdfPath;
    
    public static void main(String[] args) throws IOException {
        String sourceFolder = "";
        String archiveFolder = "sourceArchive";
        String reportsFolder = "reports";
        for (int i=0; i<args.length-1; i+=2) {
            if ("-s".equals( args[i] )) {
                sourceFolder = args[i+1];
            } else if ("-a".equals( args[i] )) {
                archiveFolder = args[i+1];
            } else if ("-r".equals( args[i] )) {
                reportsFolder = args[i+1];
            }
        }
        Path sourcePath = Paths.get( sourceFolder );
        Path archivePath = Paths.get( archiveFolder );
        Path reportsPath = Paths.get( reportsFolder );
        if (!Files.exists( sourcePath )) {
            Files.createDirectories( sourcePath );
        }
        if (!Files.exists( archivePath )) {
            Files.createDirectories( archivePath );
        }
        if (!Files.exists( reportsPath )) {
            Files.createDirectories( reportsPath );
        }
        System.out.println("watching for new files at "+sourcePath.toAbsolutePath().toString());
        WatchDir.WatchEventListener listener = ev -> {
            Path path = ev.context().toAbsolutePath();
            try {
                if (ev.kind() == StandardWatchEventKinds.ENTRY_CREATE &&
                                path.getFileName().toString().toLowerCase().endsWith( ".pdf" )) {
                    if (firstPdfPath == null) {
                        firstPdfPath = path;
                    } else {
                        PDFComparer comparer = new PDFComparer();
                        String datetime = DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( LocalDateTime.now() );
                        comparer.addPresenter( new DifferencesPDFPresenter( new File(reportsPath.toFile(), "ComparisonReports-"+datetime) ) );
                        ResultModel result = comparer.compare( firstPdfPath.toFile(), path.toFile() );
                        System.out.println("Compared "+firstPdfPath.toString()+" to "+path.toString()+".");
                        System.out.println("Differences found: "+result.getDifferencesCount( false ));
                        Files.move( firstPdfPath, archivePath.resolve( firstPdfPath.getFileName() ) );
                        Files.move( path, archivePath.resolve( path.getFileName() ) );
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        };
        WatchDir watcher = new WatchDir( sourcePath, false, listener);
        watcher.processEvents();
    }
}
