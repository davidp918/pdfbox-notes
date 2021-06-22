import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.contentstream.PDFStreamEngine;
import org.apache.pdfbox.contentstream.operator.DrawObject;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.contentstream.operator.state.Concatenate;
import org.apache.pdfbox.contentstream.operator.state.Restore;
import org.apache.pdfbox.contentstream.operator.state.Save;
import org.apache.pdfbox.contentstream.operator.state.SetGraphicsStateParameters;
import org.apache.pdfbox.contentstream.operator.state.SetMatrix;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.util.Matrix;

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

    // constructor
    public Image() throws IOException {
        addOperator(new Concatenate());
        addOperator(new DrawObject());
        addOperator(new SetGraphicsStateParameters());
        addOperator(new Save());
        addOperator(new Restore());
        addOperator(new SetMatrix());
    }

    // override the default processOperator
    @Override
    protected void processOperator(Operator operator, List<COSBase> operands) throws IOException {
        String operation = operator.getName();
        if ("Do".equals(operation)) {
            COSName objectName = (COSName) operands.get(0);
            PDXObject xobject = getResources().getXObject(objectName);
            if (xobject instanceof PDImageXObject) {
                PDImageXObject image = (PDImageXObject) xobject;
                int imageWidth = image.getWidth();
                int imageHeight = image.getHeight();
                Matrix ctmNew = getGraphicsState().getCurrentTransformationMatrix();
                float imageXScale = ctmNew.getScalingFactorX();
                float imageYScale = ctmNew.getScalingFactorY();

                // position of image in the PDF in terms of user space units
                System.out.println("position in PDF = " + ctmNew.getTranslateX() + ", " + ctmNew.getTranslateY()
                        + " in user space units");
                // raw size in pixels
                System.out.println("raw image size  = " + imageWidth + ",  " + imageHeight + " in pixels");
                // displayed size in user space units
                System.out.println("displayed size  = " + imageXScale + ",  " + imageYScale + " in user space units");
            } else if (xobject instanceof PDFormXObject) {
                PDFormXObject form = (PDFormXObject) xobject;
                showForm(form);
            }
        }
        super.processOperator(operator, operands);
    }

    public static void getLocationsAndSize() throws IOException {
        String path = String.format("pdfs/document.pdf");
        File file = new File(path);
        PDDocument doc = PDDocument.load(file);

        // instantiating and calling itself, accessing the extended methods from
        // PDFStreamEngine
        Image self = new Image();
        for (PDPage page : doc.getPages())
            self.processPage(page);

        doc.close();
    }
}
