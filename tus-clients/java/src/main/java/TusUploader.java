import io.github.cdimascio.dotenv.Dotenv;
import io.tus.java.client.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

class TusUploader {
    
    public void TusUploader() {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .directory("../../.env")
                    .load();

            HashMap<String, String> headerMap = new HashMap<>();
            headerMap.put("Authorization", "Bearer " + dotenv.get("AUTH_TOKEN"));

            // Both of these are necessary to work around 411 issue.
            System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
            headerMap.put("Content-Length", "0");

            HashMap<String, String> metadataMap = new HashMap<>();
            metadataMap.put("filename", "10MB-test-file");
            metadataMap.put("filetype", "text/plain");
            metadataMap.put("meta_destination_id", "ndlp");
            metadataMap.put("meta_ext_event", "routineImmunization");
            metadataMap.put("meta_ext_source", "IZGW");
            metadataMap.put("meta_ext_sourceversion", "V2022-12-31");
            metadataMap.put("meta_ext_entity", "DD2");
            metadataMap.put("meta_username", "ygj6@cdc.gov");
            metadataMap.put("meta_ext_filename", "10MB-test-file");
            metadataMap.put("meta_ext_objectkey", UUID.randomUUID().toString());

            System.setProperty("http.strictPostRedirect", "true");

            TusClient client = new TusClient();
            client.setUploadCreationURL(new URL(dotenv.get("DEX_URL") + "/upload"));
            client.setHeaders(headerMap);

            File file = new File("../../upload-files/10MB-test-file");
            final TusUpload upload = new TusUpload(file);
            upload.setMetadata(metadataMap);

            System.out.println("Starting upload...");

            TusExecutor executor = new TusExecutor() {
                @Override
                protected void makeAttempt() throws ProtocolException, IOException {
                    TusUploader uploader = client.resumeOrCreateUpload(upload);
                    uploader.setChunkSize(1024);

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
            e.printStackTrace();
        }
    }
}
