import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import java.nio.file.Files;

public class create_pdf {
    public static void create(String fileName) throws IOException {
        PDDocument doc = new PDDocument();
        File directory = new File("/pdfs/");
        System.out.println(directory.exists());
        if (directory.exists() == false)
            directory.mkdir();
        String path = String.format("pdfs/%s.pdf", fileName);
        System.out.println(path);
        doc.save(path);
        doc.close();
    }
}
