

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;

import io.github.cdimascio.dotenv.Dotenv;
import io.tus.java.client.ProtocolException;
import io.tus.java.client.TusClient;
import io.tus.java.client.TusExecutor;
import io.tus.java.client.TusURLMemoryStore;
import io.tus.java.client.TusUpload;
import io.tus.java.client.TusUploader;
import org.json.JSONObject;

/**
 * A representative Example class to show an usual usecase.
 */
public final class Main {
    /**
     * Main method to run a standard upload task.
     * @param
     */
    public static void main(String[] args) {
        TusUploaderProcess.makeUpload();
    }


    private Main() {
        throw new IllegalStateException("Utility class");
    }
}