import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.swing.text.Document;

import org.apache.pdfbox.multipdf.Splitter;
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

    public static void split(String fileName) throws IOException {
        String path = String.format("pdfs/%s.pdf", fileName);
        File file = new File(path);
        PDDocument doc = PDDocument.load(file);

        // instantiate the Splitter, which will be used for splitting pages
        Splitter splitter = new Splitter();
        List<PDDocument> splitted = splitter.split(doc);
        Iterator<PDDocument> splittedIterator = splitted.listIterator();

        int index = 1;
        while (splittedIterator.hasNext()) {
            PDDocument curDoc = splittedIterator.next();
            curDoc.save("pdfs/" + fileName + "-split-" + index++ + ".pdf");
        }

        doc.close();
    }

    public static void merge(String[] fileNames) throws IOException {
    }
}
