import io.github.cdimascio.dotenv.Dotenv;
import io.tus.java.client.TusClient;
import io.tus.java.client.TusUpload;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class UploadFileTest {

    private static TusClient client;

    @BeforeAll
    static void setup() {
        // Load environment variables from .env file
        Dotenv dotenv = Dotenv.configure().directory("../../").load();
        // Initialize the TusClient
        client = new TusClient();
    }

    @Test
    void makeUploadTest() {

        //Check if the file exists
        File file = new File("../../upload-files/1MB-test-file.txt");
        Assertions.assertTrue(file.exists());

        // Check if the upload URL is not null
        Assertions.assertNotNull(uploader.getUploadURL(), dotenv.get("DEX_URL") + "upload");

        // Check if the upload URL is valid
        Assertions.assertTrue(uploader.getUploadURL().toString().startsWith(dotenv.get("DEX_URL") + "upload"), true);

        //Check if the upload is successfull
        TusUploaderProcess.makeUpload();

        Assertions.assertTrue(TusUploaderProcess.isTheFileCreated());


    }
}
