import io.github.cdimascio.dotenv.Dotenv;
import io.tus.java.client.*;


public class Main {
    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();

        TusClient client = new TusClient();

      TusUploaderProcess tusUploader = new TusUploaderProcess(dotenv,client);

      tusUploader.makeUpload();

      System.out.println("Upload end---"  );
    }
}