import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.contentstream.PDFStreamEngine;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;

public class Image extends PDFStreamEngine {
    public static void insert(String fileName, int pageIndex, String imagePath) throws IOException {
        String path = String.format("pdfs/%s.pdf", fileName);
        File file = new File(path);
        PDDocument doc = PDDocument.load(file);
        PDPage page = doc.getPage(pageIndex);

        // use the PDImageXobject to perform image related operations
        PDImageXObject imageXObject = PDImageXObject.createFromFile(imagePath, doc);
        // use the content stream to display the image object
        PDPageContentStream contentStream = new PDPageContentStream(doc, page);
        // draw the image at x,y
        contentStream.drawImage(imageXObject, 0, 0);

        // dispose
        contentStream.close();
        doc.save(path);
        doc.close();
    }

    public static void extract(String fileName, int pageIndex, String destination) throws IOException {
        String path = String.format("pdfs/%s.pdf", fileName);
        File file = new File(path);
        PDDocument doc = PDDocument.load(file);

        // use the PDFRenderer class to render the images in the pdf
        PDFRenderer renderer = new PDFRenderer(doc);
        BufferedImage image = renderer.renderImage(pageIndex);
        // write the image to a file at the desired destination
        ImageIO.write(image, "jpg", new File(destination));

        doc.close();
    }

    public static void getLocationAndSize(String fileName) throws IOException {
        String path = String.format("pdfs/%s.pdf", fileName);
        File file = new File(path);
        PDDocument doc = PDDocument.load(file);

        int i = 0;
        for (PDPage page : doc.getPages()) {

        }
    }
}
