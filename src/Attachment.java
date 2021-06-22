import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentNameDictionary;
import org.apache.pdfbox.pdmodel.PDEmbeddedFilesNameTreeNode;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.common.filespecification.PDEmbeddedFile;

public class Attachment {
    public static void add(String fileName) {
        String path = String.format("pdfs/%s.pdf", fileName);
        File file = new File(path);
        try {
            PDDocument doc = PDDocument.load(file);

            // First, create a dummy byte file
            byte[] data = "Dummy content file data information purpose dummy file data".getBytes();
            ByteArrayInputStream byteFile = new ByteArrayInputStream(data);
            // create the embedded file and set some attributes
            PDEmbeddedFile embeddedFile = new PDEmbeddedFile(doc, byteFile);
            embeddedFile.setSubtype("test/plain");
            embeddedFile.setSize(data.length);
            embeddedFile.setCreationDate(new GregorianCalendar());
            // create the file specification, which holds the embedded file
            PDComplexFileSpecification fileSpecification = new PDComplexFileSpecification();
            fileSpecification.setFile("dummy.txt");
            fileSpecification.setEmbeddedFile(embeddedFile);

            // Next, create a map where: key -> name, value -> specification
            Map<String, PDComplexFileSpecification> map = Collections.singletonMap("Dummy", fileSpecification);
            // and add the map to a tree node
            PDEmbeddedFilesNameTreeNode embeddedFilesNameTreeNode = new PDEmbeddedFilesNameTreeNode();
            embeddedFilesNameTreeNode.setNames(map);

            // store the attachment as part of the *Names directory in the document catalog
            PDDocumentNameDictionary nameDictionary = new PDDocumentNameDictionary(doc.getDocumentCatalog());
            nameDictionary.setEmbeddedFiles(embeddedFilesNameTreeNode);
            doc.getDocumentCatalog().setNames(nameDictionary);

            doc.save(path);
            doc.close();
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    public static Map<String, PDComplexFileSpecification> get(String fileName) throws IOException {
        String path = String.format("pdfs/%s.pdf", fileName);
        File file = new File(path);
        PDDocument doc = PDDocument.load(file);

        PDDocumentNameDictionary dictionary = new PDDocumentNameDictionary(doc.getDocumentCatalog());
        PDEmbeddedFilesNameTreeNode treeNode = dictionary.getEmbeddedFiles();
        Map<String, PDComplexFileSpecification> map = treeNode.getNames();

        return map;
    }
}
