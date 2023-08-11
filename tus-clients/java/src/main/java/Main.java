import io.github.cdimascio.dotenv.Dotenv;
import io.tus.java.client.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;


public class Main {
    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();

        TusClient client = new TusClient();
        TusUploaderProcess tusUploader = new TusUploaderProcess(dotenv,client);
        tusUploader.makeUpload();


      System.out.println("Upload end---"  );
    }
}