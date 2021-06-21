import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class AddText {
    public static void addOneLineText(String fileName, int pageIndex, String text, Float[] coordinate)
            throws IOException {
        String path = String.format("pdfs/%s.pdf", fileName);
        File file = new File(path);
        PDDocument doc = Loader.loadPDF(file);
        PDPage page = doc.getPage(pageIndex);

        // content stream is for inserting data to the *page of the *doc
        PDPageContentStream contentStream = new PDPageContentStream(doc, page);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_BOLD_ITALIC, 14);
        contentStream.newLineAtOffset(coordinate[0], coordinate[1]); // xy coordinate from bottom left
        contentStream.showText(text); // add the text
        contentStream.endText(); // end the text
        contentStream.close(); // closing the stream

        doc.save(path); // default saveing to the top directory of the project
        doc.close(); // dispose
    }
}
