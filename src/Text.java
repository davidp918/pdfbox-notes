import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;

public class Text {
    public static void addOneLine(String fileName, int pageIndex, String text, Float[] coordinate) throws IOException {
        String path = String.format("pdfs/%s.pdf", fileName);
        File file = new File(path);
        PDDocument doc = PDDocument.load(file);
        PDPage page = doc.getPage(pageIndex);

        // content stream is for inserting data to the *page of the *doc
        PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.OVERWRITE,
                true);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_BOLD_ITALIC, 14);
        contentStream.newLineAtOffset(coordinate[0], coordinate[1]); // xy coordinate from bottom left
        contentStream.showText(text); // add the text
        contentStream.endText(); // end the text
        contentStream.close(); // closing the stream

        doc.save(new File(path)); // default saveing to the top directory of the project
        // default saveing to the top directory of the project
        doc.close(); // dispose
    }

    public static void addMultipleLine(String fileName, int pageIndex, String text, Float[] coordinate)
            throws IOException {
        String path = String.format("pdfs/%s.pdf", fileName);
        File file = new File(path);
        PDDocument doc = PDDocument.load(file);
        PDPage page = doc.getPage(pageIndex);

        // content stream is for inserting data to the *page of the *doc
        PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.OVERWRITE,
                true);
        contentStream.beginText();
        // set text font
        contentStream.setFont(PDType1Font.COURIER_BOLD_OBLIQUE, 14);
        // set xy pos coordinate from bottom left
        contentStream.newLineAtOffset(coordinate[0], coordinate[1]);
        // set space between lines vertically
        contentStream.setLeading(14.5f);
        contentStream.showText(text);
        contentStream.newLine();
        contentStream.showText(text);
        contentStream.endText(); // end the text
        contentStream.close(); // closing the stream

        doc.save(path); // default saveing to the top directory of the project
        doc.close(); // dispose
    }

    public static String extractText(String fileName) throws IOException {
        String path = String.format("pdfs/%s.pdf", fileName);
        File file = new File(path);
        PDDocument doc = PDDocument.load(file);

        // used to retrieve text from pdf
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(doc);

        doc.close();
        return text;
    }

    public static List<String> extractPassport(String fileName) throws IOException {
        String path = String.format("pdfs/%s.pdf", fileName);
        File file = new File(path);
        PDDocument doc = PDDocument.load(file);

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