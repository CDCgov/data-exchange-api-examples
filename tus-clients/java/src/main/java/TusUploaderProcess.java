

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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

/**
 * A representative Example class to show an usual usecase.
 */
public class TusUploaderProcess {

    private static boolean uploadSuccessful = false;
    /**
     * makeUpload method to run a standard upload task.
     * @param
     */
    public static void makeUpload() {
        try {
            // When Java's HTTP client follows a redirect for a POST request, it will change the
            // method from POST to GET which can be disabled using following system property.
            // If you do not enable strict redirects, the tus-java-client will not follow any
            // redirects but still work correctly.
            System.setProperty("http.strictPostRedirect", "true");
            Dotenv dotenv = Dotenv.configure().directory("../../").load();

            String accessToken = "";

            String username = dotenv.get("ACCOUNT_USERNAME");
            String password = dotenv.get("ACCOUNT_PASSWORD");
            String apiUrl = dotenv.get("DEX_URL");
            HashMap<String, String> headerMap = new HashMap<>();

            // Both of these are necessary to work around the 411 issue.
            System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
            headerMap.put("Content-Length","0");

            // Create a new TusClient instance
            final TusClient client = new TusClient();

            // Configure tus HTTP endpoint. This URL will be used for creating new uploads
            client.setUploadCreationURL(new URL(apiUrl + "upload"));

            File file = new File("../../upload-files/1MB-test-file.txt");

            // Enable resumable uploads by storing the upload URL in memory
            client.enableResuming(new TusURLMemoryStore());

            // Open a file using which we will then create a TusUpload. If you do not have
            // a File object, you can manually construct a TusUpload using an InputStream.
            // See the documentation for more information.

            final TusUpload upload = new TusUpload(file);

            HashMap<String, String> metadataMap = new HashMap<>();
            metadataMap.put("filename", "1MB-test-file");
            metadataMap.put("filetype", "text/plain");
            metadataMap.put("meta_destination_id", "dextesting");
            metadataMap.put("meta_ext_event", "testevent1");
            metadataMap.put("meta_ext_source", "IZGW");
            metadataMap.put("meta_ext_sourceversion", "V2022-12-31");
            metadataMap.put("meta_ext_entity", "DD2");
            metadataMap.put("meta_username", "ygj6@cdc.gov");
            metadataMap.put("meta_ext_filename", "1MB-test-file");
            metadataMap.put("meta_ext_objectkey", UUID.randomUUID().toString());
            metadataMap.put("original_file_timestamp", String.valueOf(file.lastModified()));

            accessToken = returnAccessToken();
            headerMap.put("Authorization", "Bearer " + accessToken);

            upload.setMetadata(metadataMap);
            client.setHeaders(headerMap);

            System.out.println("Starting upload...");

            String finalAccessToken = accessToken;
            TusExecutor executor = new TusExecutor() {
                @Override
                protected void makeAttempt() throws ProtocolException, IOException {
                    // First try to resume an upload. If that's not possible we will create a new
                    // upload and get a TusUploader in return. This class is responsible for opening
                    // a connection to the remote server and doing the uploading.
                    TusUploader uploader = client.resumeOrCreateUpload(upload);

                    // Upload the file in chunks of 1KB sizes.
                    uploader.setChunkSize(1024);

                    // Upload the file as long as data is available. Once the
                    // file has been fully uploaded the method will return -1
                    do {
                        // Calculate the progress using the total size of the uploading file and
                        // the current offset.
                        long totalBytes = upload.getSize();
                        long bytesUploaded = uploader.getOffset();
                        double progress = (double) bytesUploaded / totalBytes * 100;

                        System.out.printf("Upload at %06.2f%%.\n", progress);
                    } while (uploader.uploadChunk() > -1);

                    // Allow the HTTP connection to be closed and cleaned up
                    uploader.finish();
                    URL url = new URL(apiUrl + "status/");
                    String[] split = uploader.getUploadURL().toString().split("/");
                    String testUrl = url + split[split.length-1].trim();
                    uploadSuccessful = isUploadSuccessful(testUrl, finalAccessToken);
                    System.out.println("Upload finished.");
                    System.out.format("Upload available at: %s", uploader.getUploadURL().toString());
                }
            };
            executor.makeAttempts();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String returnAccessToken(){
        Dotenv dotenv = Dotenv.configure().directory("../../").load();

        String username = dotenv.get("ACCOUNT_USERNAME");
        String password = dotenv.get("ACCOUNT_PASSWORD");
        String apiUrl = dotenv.get("DEX_URL");


        LoginClient lc = new LoginClient();
        String access;
        access = lc.login(username, password, apiUrl);
        System.out.println("accessToken...[" + access + "]");
        String trimmedAccessToken = access.trim();

        return trimmedAccessToken;
    }

    public static boolean isUploadSuccessful(String url, String accessToken){
        System.out.println("url in : " + url);
        System.out.println("accessToken in : " + accessToken);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + accessToken)
                .build();
        try (Response response = client.newCall(request).execute()) {
            int statusCode = response.code();
            //String responseBody = response.body().string();
            System.out.println("Response status code: " + statusCode);
            //System.out.println("Response body: " + responseBody);

            if(statusCode == 200)
                uploadSuccessful=true;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return uploadSuccessful;
    }

    public static boolean isTheFileCreated(){
        return uploadSuccessful;
    }

}