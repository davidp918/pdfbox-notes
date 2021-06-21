public class App {
    public static void main(String[] args) throws Exception {
        Init.create("test", 4);
        AddText.addOneLineText("test", 2, "Sample one line text", new Float[] { 20.0f, 100.0f });
    }
}
