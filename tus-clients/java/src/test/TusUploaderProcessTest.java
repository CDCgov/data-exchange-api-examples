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


public class TusUploaderProcessTest {

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
        File file = new File("../../upload-files/large_file_1MB_001.txt");
        Assertions.assertTrue(file.exists());

        // Check if the upload URL is not null
        Assertions.assertNotNull(uploader.getUploadURL(), "https://apistg.cdc.gov/upload");

        // Check if the upload URL is valid
        Assertions.assertTrue(uploader.getUploadURL().toString().startsWith(dotenv.get("DEX_URL") + "upload"),
                "https://apistg.cdc.gov/upload");

        //Check if the upload is successfull
        TusUploaderProcess.makeUpload();

        Assertions.assertTrue(TusUploaderProcess.isUploadSuccessfull());


    }
}
