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

    private static Dotenv dotenv;
    private static TusClient client;

    @BeforeAll
    static void setup() {
        // Load environment variables from .env file
        dotenv = Dotenv.load();
        // Initialize the TusClient
        client = new TusClient();
    }

    @Test
    void makeUploadTest() {
        TusUploaderProcess uploader = new TusUploaderProcess(dotenv, client);
        uploader.makeUpload();

        // Assertion 1: Check if the upload URL is not null
        Assertions.assertNotNull(uploader.getUploadURL(), "https://apidev.cdc.gov/upload");

        // Assertion 2: Check if the upload URL is valid
        Assertions.assertTrue(uploader.getUploadURL().toString().startsWith(dotenv.get("DEX_URL") + "upload"),
                "https://apidev.cdc.gov/upload");

        // Assertion 3: Check if the upload was successful (assuming a specific response format)
        String response = uploader.getUploadResponse(); // Assuming you have a method to get the response content
        Assertions.assertTrue(response.contains("Upload successful"), "Upload should be successful");

        // Assertion 4: Check if the uploaded file exists on the server (if applicable)
        // You can make an HTTP request to the upload URL to check if the file exists.

        // Add more assertions based on the specific requirements of the upload process.
    }
}
