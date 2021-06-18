
import java.io.IOException;
public class create_pdf{
    public static void main(String fileName) throws IOException{
        PDDocument document = new PDDocument();
        document.save(String.format('/pdfs/%s', fileName));
        document.close();
    }
}
