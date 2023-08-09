import com.sun.net.httpserver.Headers;
import io.github.cdimascio.dotenv.Dotenv;
import io.tus.java.client.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

class TusUploaderProcess {
    private Dotenv dotenv;
    private TusClient client ;

    public TusUploaderProcess(Dotenv dotenv, TusClient client) {
        this.dotenv = dotenv;
        this.client = client;
    }

    public void makeUpload() {
        try {
            LoginClient loginClient = new LoginClient();
            String username = dotenv.get("ACCOUNT_USERNAME");
            String password = dotenv.get("ACCOUNT_PASSWORD");
            String apiUrl = dotenv.get("DEX_URL");
            System.out.println("apiUrl--" + apiUrl );

            String accessToken = loginClient.login(username, password, apiUrl);
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap.put("Authorization", "Bearer " + accessToken);

            System.out.println("Authorization ..." + headerMap.get("Authorization"));
            client.setUploadCreationURL(new URL(apiUrl + "upload"));
            //client.enableResuming(new TusURLMemoryStore());

            File file = new File("../../upload-files/10MB-test-file");
            long fileSize = file.length();
            System.out.println("fileSize ..." + fileSize );
            String stringSize =String.valueOf(fileSize);
            // Both of these are necessary to work around the 411 issue.
            System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
            headerMap.put("Content-Length",stringSize);
            //headerMap.put("Upload-Offset", "0");

            client.setHeaders(headerMap);
            System.out.println("headers ..." + client.getHeaders() );
            final TusUpload upload = new TusUpload(file);

            HashMap<String, String> metadataMap = new HashMap<>();
            metadataMap.put("filename", "10MB-test-file");
            metadataMap.put("filetype", "text/plain");
            metadataMap.put("meta_destination_id", "dextesting");
            metadataMap.put("meta_ext_event", "testevent1");
            metadataMap.put("meta_ext_source", "IZGW");
            metadataMap.put("meta_ext_sourceversion", "V2022-12-31");
            metadataMap.put("meta_ext_entity", "DD2");
            metadataMap.put("meta_username", "ygj6@cdc.gov");
            metadataMap.put("meta_ext_filename", "10MB-test-file");
            metadataMap.put("meta_ext_objectkey", UUID.randomUUID().toString());
            metadataMap.put("original_file_timestamp", String.valueOf(file.lastModified()));
            //metadataMap.put("meta_ext_submissionperiod", "1");

            System.setProperty("http.strictPostRedirect", "true");
            upload.setMetadata(metadataMap);
            //client.enableResuming(new TusURLMemoryStore());
            //System.out.println("upload ..." + upload.getMetadata());
            upload.setSize(fileSize);
            System.out.println("upload url..." + client.getUploadCreationURL());
            System.out.println("Starting upload ...");
            TusExecutor executor = new TusExecutor() {
                @Override
                protected void makeAttempt() throws ProtocolException, IOException {
                    System.out.println("Before Starting upload ...");
                    System.out.println("Before fileSize ..." + fileSize);
                    upload.setSize(fileSize);
                    TusUploader uploader = client.resumeOrCreateUpload(upload);
                    System.out.println("Starting upload offset..." + uploader.getOffset());
                    uploader.setChunkSize(1024  * 1024 );
                    do {
                        long totalBytes = upload.getSize();
                        long bytesUploaded = uploader.getOffset();
                        double progress = (double) bytesUploaded / totalBytes * 100;

                        System.out.printf("Upload at %06.2f%%.\n", progress);
                    } while (uploader.uploadChunk() > -1);

                    uploader.finish();

                    System.out.println("Upload finished.");
                    System.out.format("Upload available at: %s", uploader.getUploadURL().toString());
                }
            };

            executor.makeAttempts();

        } catch (Exception e) {
            System.out.println("last catch Upload finished." );
            e.printStackTrace();
        }
    }


}
