import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

public class remove {
    public static void removePage(String fileName, int index) throws IOException {
        String path = String.format("pdfs/%s.pdf", fileName);
        File file = new File(path);
        PDDocument doc = Loader.loadPDF(file);

        int pageCount = doc.getNumberOfPages();
        if (index < pageCount)
            doc.removePage(index);

        doc.save(file);
        doc.close();

    }
}
