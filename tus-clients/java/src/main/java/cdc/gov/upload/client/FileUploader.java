package cdc.gov.upload.client;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import cdc.gov.upload.client.model.FileStatus;
import cdc.gov.upload.client.tus.TusUploadExecutor;
import cdc.gov.upload.client.utils.LoginUtil;
import cdc.gov.upload.client.utils.StatusUtil;

public class FileUploader {

    public static void main(String[] args) {

        try {
            Properties prop = new Properties();
            prop.load(LoginUtil.class.getClassLoader().getResourceAsStream("env.properties"));

            String username = System.getProperty("USERNAME");
            if(username == null || username.isEmpty()){
                username = prop.getProperty("USERNAME");
            }

            String password = System.getProperty("PASSWORD");
            if(password == null || password.isEmpty()){
                password = prop.getProperty("PASSWORD");
            }

            String baseUrl = System.getProperty("URL");
            if(baseUrl == null || baseUrl.isEmpty()) {
                baseUrl = prop.getProperty("URL");                
            }

            System.out.println("username: " + username);
            if(password != null && !password.isEmpty()) {
                System.out.println("password: *****");
            } else {
                System.out.println("password: " + password);
            }
            
            System.out.println("baseUrl: " + baseUrl);

            System.out.println("Getting Token");
            String token = LoginUtil.getToken(username, password, baseUrl);

            TusUploadExecutor tusUploadExecutor = new TusUploadExecutor();

            File file = getFileToUpload("1MB-test-file");

            Map<String, String> metadata = getMetadata("dextesting", "testevent1", file);

            tusUploadExecutor.initiateUpload(token, baseUrl, file, metadata);

            String tguid = tusUploadExecutor.getTguid();
            System.out.println("TGUID Received: " + tguid);
                    
            System.out.println("Checking file Status");
            FileStatus fileStatus = StatusUtil.getFileStatus(token, tguid, baseUrl);

            String status = fileStatus.getStatus();

            if(status.equalsIgnoreCase("Complete")) {
                
                System.out.println("File Uploaded Successfully!");
            }

        } catch (Throwable t) {

            t.printStackTrace();
        }
    }

    private static File getFileToUpload(String fileName) {        
        try {
            URL resource = LoginUtil.class.getClassLoader().getResource(fileName);
            
            if (resource == null) {

                System.err.println("upload file not found!");
            } else {

                return new File(resource.toURI());
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    private static Map<String, String> getMetadata(String destination, String event, File file) {

        HashMap<String, String> metadataMap = new HashMap<>();
        metadataMap.put("meta_destination_id", destination);
        metadataMap.put("meta_ext_event", event);        
        metadataMap.put("filename", file.getName());
        metadataMap.put("filetype", "text/plain");

        metadataMap.put("meta_ext_source", "INTEGRATION-TEST");
        metadataMap.put("meta_ext_filename", file.getName());
        metadataMap.put("meta_ext_objectkey", UUID.randomUUID().toString());
        metadataMap.put("original_file_timestamp", String.valueOf(file.lastModified()));

        return metadataMap;
    }    
}
