import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class Page {
    public static void remove(String fileName, int index) throws IOException {
        String path = String.format("pdfs/%s.pdf", fileName);
        File file = new File(path);
        PDDocument doc = PDDocument.load(file);

        int pageCount = doc.getNumberOfPages();
        if (index < pageCount)
            doc.removePage(index);

        doc.save(path);
        doc.close();
    }

    public static void add(String fileName, int pageCount) throws IOException {
        String path = String.format("pdfs/%s.pdf", fileName);
        File file = new File(path);
        PDDocument doc = PDDocument.load(file);

        for (int i = 0; i < pageCount; i++) {
            PDPage blankPage = new PDPage(); // instantiate a new page
            doc.addPage(blankPage); // adding a pagepage
        }

        doc.save(new File(path));
        doc.close();
    }
}
