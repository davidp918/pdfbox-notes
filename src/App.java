public class App {
    public static void main(String[] args) throws Exception {
        String fileName = "tet";
        Init.create(fileName, 4);
        /*
         * AddText.addOneLineText(fileName, 0, "Sample one line text", new Float[] {
         * 20.0f, 100.0f }); AddText.addMultipleLineText(fileName, 1,
         * "Sample multiple lines texts texts sample", new Float[] { 100.0f, 700.0f });
         * 
         * remove.removePage(fileName, 2); System.out.println(Extract.readText("CAS"));
         * System.out.println(Extract.passport("CAS"));
         * System.out.println(MetaData.set(fileName));
         * System.out.println(MetaData.get(fileName));
         */
        Attachment.add(fileName);
        System.out.println(Attachment.get(fileName));
    }
}
