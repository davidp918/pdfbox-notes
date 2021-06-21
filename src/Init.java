import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class Init {
    public Init() {

    }

    public static void create(String fileName, int pageCount) throws IOException {
        // Initialization
        String path = String.format("pdfs/%s.pdf", fileName);
        PDDocument doc = new PDDocument();

        for (int i = 0; i < pageCount; i++) {
            PDPage blankPage = new PDPage(); // instantiate a new page
            doc.addPage(blankPage); // adding a pagepage
        }
        doc.save(path); // default saveing to the top directory of the project
        doc.close(); // dispose
    }
}
