import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class Extract {
    public static String text(String fileName) throws IOException {
        String path = String.format("pdfs/%s.pdf", fileName);
        File file = new File(path);
        PDDocument doc = Loader.loadPDF(file);

        // used to retrieve text from pdf
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(doc);

        doc.close();
        return text;
    }

    public static List<String> passport(String fileName) throws IOException {
        String path = String.format("pdfs/%s.pdf", fileName);
        File file = new File(path);
        PDDocument doc = Loader.loadPDF(file);

        StringBuilder sBuilder = new StringBuilder();
        PDFTextStripper stripper = new PDFTextStripper();

        sBuilder.append(stripper.getText(doc));
        // . - any, \\d - digit
        Pattern pattern = Pattern.compile("..\\d\\d\\d\\d\\d\\d\\d");
        Matcher matcher = pattern.matcher(sBuilder);
        List<String> res = new ArrayList<String>();
        while (matcher.find())
            res.add(matcher.group());

        doc.close();
        return res;
    }
}
