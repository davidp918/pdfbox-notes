import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.spi.CalendarNameProvider;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentNameDictionary;
import org.apache.pdfbox.pdmodel.PDEmbeddedFilesNameTreeNode;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.common.filespecification.PDEmbeddedFile;

public class Attachments {
    public static void set(String fileName) throws IOException {
        String path = String.format("pdfs/%s.pdf", fileName);
        File file = new File(path);
        PDDocument doc = Loader.loadPDF(file);
        // attachments are stored in the dictionary as names
        PDDocumentNameDictionary dictionary = new PDDocumentNameDictionary(doc.getDocumentCatalog());
        // create the file and add it to the document
        PDEmbeddedFilesNameTreeNode dummyNodes = createDummy(doc);
        dictionary.setEmbeddedFiles(dummyNodes);
        doc.getDocumentCatalog().setNames(dictionary);

        doc.save(path);
        doc.close();
    }

    public static Map<String, PDComplexFileSpecification> get(String fileName) throws IOException {
        String path = String.format("pdfs/%s.pdf", fileName);
        File file = new File(path);
        PDDocument doc = Loader.loadPDF(file);

        // print out all the files
        PDDocumentNameDictionary dictionary = new PDDocumentNameDictionary(doc.getDocumentCatalog());
        PDEmbeddedFilesNameTreeNode treeNode = dictionary.getEmbeddedFiles();
        Map<String, PDComplexFileSpecification> fileNames = treeNode.getNames();

        return fileNames;
    }

    private static PDEmbeddedFilesNameTreeNode createDummy(PDDocument doc) throws IOException {
        // creating a dummy file to add
        byte[] data = "Dummy content file data information context file example purpose dummy file data".getBytes();
        ByteArrayInputStream fileStream = new ByteArrayInputStream(data);
        // instantiate the embedded file using the dummy file stream, specifying the
        // target doc
        PDEmbeddedFile embeddedFile = new PDEmbeddedFile(doc, fileStream);
        embeddedFile.setSize(data.length);
        embeddedFile.setSubtype("text/plain");
        // add the file to a new specification
        PDComplexFileSpecification newComplexFileSpecification = new PDComplexFileSpecification();
        newComplexFileSpecification.setEmbeddedFile(embeddedFile);
        // add the new specification to a new node
        PDEmbeddedFilesNameTreeNode newChildNode = new PDEmbeddedFilesNameTreeNode();
        newChildNode.setNames(Collections.singletonMap("Dummy file", newComplexFileSpecification));
        // add the node to a list of nodes
        List<PDEmbeddedFilesNameTreeNode> listChildNodes = new ArrayList<PDEmbeddedFilesNameTreeNode>();
        listChildNodes.add(newChildNode);
        // add the list of child nodes to a tree as its kids
        PDEmbeddedFilesNameTreeNode treeNode = new PDEmbeddedFilesNameTreeNode();
        treeNode.setKids(listChildNodes);

        return treeNode;

    }
}
