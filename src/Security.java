import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import org.apache.pdfbox.multipdf.Overlay;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;

import itext.PdfSignItext;

public class Security {
    /**
     * Add Watermark
     * 
     * @param fileName
     * @param watermarkText
     * @throws IOException
     */
    public static void addWatermark(String fileName, String watermarkText) throws IOException {
        // load the file going to be watermarked
        String path = String.format("pdfs/%s.pdf", fileName);
        File file = new File(path);
        PDDocument doc = PDDocument.load(file);
        doc.setAllSecurityToBeRemoved(true);
        String watermark = "pdfs/" + watermarkText + ".pdf";
        File watermarkFile = new File(watermark);
        if (!watermarkFile.exists())
            createWaterMark(watermarkText);

        // get the pages and create a map with page number and the watermark image using
        // existing pdf
        HashMap<Integer, String> overlays = new HashMap<Integer, String>();
        for (int i = 0; i < doc.getNumberOfPages(); i++)
            overlays.put(i + 1, watermark);

        // use the Overlay objects to add watermarks to the doc
        Overlay overlay = new Overlay();
        overlay.setInputPDF(doc);
        overlay.setOverlayPosition(Overlay.Position.FOREGROUND);
        overlay.overlay(overlays);

        overlay.close();
        doc.save(path);
        doc.close();
    }

    /**
     * Encrypte and protext the pdf file with passwords
     * 
     * @param fileName  name of the file to be encrypted
     * @param ownerPass password for opening the file with all permissions
     * @param userPass  password for opening the file with limited permissions
     */
    public static void encrypte(String fileName, String ownerPassword, String userPassword) throws IOException {
        String path = String.format("pdfs/%s.pdf", fileName);
        File file = new File(path);
        PDDocument doc = PDDocument.load(file);
        // define the length of the encryption key
        int keyLength = 256;
        AccessPermission permission = new AccessPermission();
        permission.setCanPrint(false); // disable printing, anything else is allowed
        StandardProtectionPolicy policy = new StandardProtectionPolicy(ownerPassword, userPassword, permission);
        policy.setEncryptionKeyLength(keyLength);
        policy.setPermissions(permission);

        doc.protect(policy);
        doc.save(path);
        doc.close();
    }

    /**
     * Used third party package to achieve digital certificate signing
     * 
     * @param source
     * @param destination
     * @param KEYSTORE
     * @param PASSWORD
     * @param reason
     * @param location
     * @param chapterPath
     * @throws Exception
     */
    public static void sign(String source, String destination, String KEYSTORE, String PASSWORD, String reason,
            String location, String chapterPath) throws Exception {
        source = "pdfs/" + source + ".pdf";
        destination = "pdfs/" + destination + ".pdf";
        PdfSignItext.sign(new FileInputStream(source), new FileOutputStream(destination), new FileInputStream(KEYSTORE),
                PASSWORD.toCharArray(), reason, location, chapterPath);
    }

    public static void crossPageSeal(String fileName, String seal) throws IOException {
        String path = String.format("pdfs/%s.pdf", fileName);
        File file = new File(path);
        PDDocument doc = PDDocument.load(file);
        int pageCount = doc.getNumberOfPages();

        // cut original seal into equally wide sub-seals
        BufferedImage image = ImageIO.read(new File(seal));
        float sealHeight = image.getHeight();
        float sealWidth = image.getWidth();
        float sliceWidth = sealWidth / pageCount;

        for (int i = 0; i < pageCount; i++) {
            BufferedImage curSubSeal = image.getSubimage((int) ((i + 1) * sliceWidth), 0, (int) sliceWidth,
                    (int) sealHeight);
            PDPage curPage = doc.getPage(i);
            float tx = curPage.getMediaBox().getWidth() - sealWidth;
            float ty = (curPage.getMediaBox().getHeight() - sealHeight) / 2;

            PDImageXObject imageXObject = LosslessFactory.createFromImage(doc, curSubSeal);
            PDPageContentStream contentStream = new PDPageContentStream(doc, curPage);
            contentStream.drawImage(imageXObject, tx, ty);

            contentStream.close();
        }

        doc.save("pdfs/" + fileName + "_cross_seal.pdf");
        doc.close();

    }

    /**
     * Create watermark overlay pdf doc for add watermarks to other files
     * 
     * @param text
     * @throws IOException
     */
    private static void createWaterMark(String text) throws IOException {
        PDDocument overlayDoc = new PDDocument();
        PDPage page = new PDPage();
        overlayDoc.addPage(page);

        PDFont font = PDType1Font.COURIER;
        float fontSize = 80;

        float pageWidth = page.getMediaBox().getWidth();
        float pageHeight = page.getMediaBox().getHeight();
        double rotation = Math.PI / 5;

        double initTitleWidth = font.getStringWidth(text) / 1000 * fontSize;
        double initTitleHeight = fontSize;

        double widthBaseWidth = initTitleWidth * Math.cos(rotation);
        double heightBaseWidth = initTitleHeight * Math.cos(Math.PI / 2 - rotation);
        double widthSideHeight = initTitleWidth * Math.sin(rotation);
        double heightSideHeight = initTitleHeight * Math.sin(Math.PI / 2 - rotation);

        double titleWidth = widthBaseWidth + heightBaseWidth;
        double titleHeight = widthSideHeight + heightSideHeight;

        double xPos = (pageWidth - titleWidth) / 2;
        double yPos = (pageHeight - titleHeight) / 2;

        float tx = (float) (xPos + heightBaseWidth);
        float ty = (float) (yPos);

        Matrix matrix = Matrix.getRotateInstance(rotation, tx, ty);

        PDPageContentStream contentStream = new PDPageContentStream(overlayDoc, page);
        PDExtendedGraphicsState state = new PDExtendedGraphicsState();
        state.setNonStrokingAlphaConstant(0.3f);
        state.setAlphaSourceFlag(true);
        contentStream.setGraphicsStateParameters(state);

        contentStream.setFont(font, fontSize);
        contentStream.beginText();
        contentStream.setTextMatrix(matrix);
        contentStream.showText(text);
        contentStream.endText();

        contentStream.close();
        overlayDoc.save("pdfs/" + text + ".pdf");
        overlayDoc.close();
    }
}
