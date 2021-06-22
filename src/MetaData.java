import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

public class MetaData {
    static String[] parameters = { "Author", "Title", "Subject", "Creator", "Keywords", "CreationDate",
            "ModificationDate" };

    public static Boolean set(String fileName) throws IOException {
        boolean success = false;
        String path = String.format("pdfs/%s.pdf", fileName);
        File file = new File(path);
        PDDocument doc = PDDocument.load(file);
        PDDocumentInformation docInfo = doc.getDocumentInformation();

        docInfo.setCreator("David");
        docInfo.setAuthor("David");
        docInfo.setSubject("set metadata");
        Calendar date = new GregorianCalendar();
        docInfo.setCreationDate(date);
        docInfo.setModificationDate(date);
        docInfo.setKeywords("Altering, metadata");
        docInfo.setProducer("David");

        doc.save(path);
        doc.close();
        return success;
    }

    public static Map<String, Object> get(String fileName) throws Exception {
        Map<String, Object> info = new HashMap<String, Object>();
        String path = String.format("pdfs/%s.pdf", fileName);
        File file = new File(path);
        PDDocument doc = PDDocument.load(file);

        PDDocumentInformation docInfo = doc.getDocumentInformation();
        for (String parameter : parameters) {
            try {
                // try retrieving metadata
                Method method = PDDocumentInformation.class.getMethod(String.format("get%s", parameter));
                Object object = method.invoke(docInfo);
                info.put(parameter, object);
            } catch (Exception e) {
                throw e;
            }
        }

        doc.close();
        return info;
    }
}
