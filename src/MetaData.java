import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

public class MetaData {
    static String[] parameters = { "Author", "Title", "Subject", "Creator", "Keywords", "CreationDate",
            "ModificationDate" };

    public static Boolean set(String fileName, Map<String, Object> info) throws IOException {
        boolean success = false;
        String path = String.format("pdfs/%s.pdf", fileName);
        File file = new File(path);
        PDDocument doc = Loader.loadPDF(file);
        PDDocumentInformation docInfo = doc.getDocumentInformation();

        try {
            for (String parameter : parameters) {
                if (!info.containsKey(parameter))
                    continue;
                System.out.println(parameter);
                Method method = PDDocumentInformation.class.getMethod(String.format("set%s", parameter));
                method.invoke(docInfo, parameter);
            }
            doc.save(file);
            success = true;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            doc.close();
        }

        return success;
    }

    public static Map<String, Object> get(String fileName) throws Exception {
        Map<String, Object> info = new HashMap<String, Object>();
        String path = String.format("pdfs/%s.pdf", fileName);
        File file = new File(path);
        PDDocument doc = Loader.loadPDF(file);

        PDDocumentInformation docInfo = doc.getDocumentInformation();
        for (String parameter : parameters) {
            try {
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
