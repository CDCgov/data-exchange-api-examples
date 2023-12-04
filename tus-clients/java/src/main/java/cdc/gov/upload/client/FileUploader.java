package cdc.gov.upload.client;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Properties;
import cdc.gov.upload.client.model.FileStatus;
import cdc.gov.upload.client.model.Upload;
import cdc.gov.upload.client.tus.TusUploadExecutor;
import cdc.gov.upload.client.utils.DestinationsUtil;
import cdc.gov.upload.client.utils.LoginUtil;
import cdc.gov.upload.client.utils.StatusUtil;

public class FileUploader {

    private static final String DEFAULT_DESTINATION = "dextesting";
    private static final String DEFAULT_EVENT = "testevent1";

    public static void main(String[] args) {

        try {
            String username, password, baseUrl;

            Properties prop = new Properties();

            try {
                prop.load(LoginUtil.class.getClassLoader().getResourceAsStream("env.properties"));
            } catch(Exception e) {
                System.err.println("Properties file not found. Set env variables as an alternative.");
            }            

            username = System.getProperty("USERNAME");
            if(username == null || username.isEmpty()){
                username = prop.getProperty("USERNAME");
            }

            password = System.getProperty("PASSWORD");
            if(password == null || password.isEmpty()){
                password = prop.getProperty("PASSWORD");
            }

            baseUrl = System.getProperty("URL");
            if(baseUrl == null || baseUrl.isEmpty()) {
                baseUrl = prop.getProperty("URL");                
            }

            printValues(username, password, baseUrl);

            upload(args, username, password, baseUrl);

            prop.clear();

        } catch (Throwable t) {

            t.printStackTrace();
            System.exit(1);
        } 
    }

    private static void upload(String[] args, String username, String password, String baseUrl) throws Exception {

        if(username != null && !username.isEmpty() &&
           password != null && !password.isEmpty() && 
           baseUrl  != null && !baseUrl.isEmpty()) {

            System.out.println("Getting Token");
            String token = LoginUtil.getToken(username, password, baseUrl);

            TusUploadExecutor tusUploadExecutor = new TusUploadExecutor();

            String destination = null, event = null;
            if(args.length == 2) {
                destination = args[0];
                event = args[1];
            }

            if((destination == null || destination.isEmpty()) && 
                (event == null || event.isEmpty())) {
                System.out.println("No Destination && Event Provided. Using Defaults: " + DEFAULT_DESTINATION + "-" + DEFAULT_EVENT);
                destination = DEFAULT_DESTINATION;
                event = DEFAULT_EVENT;
            } else {
                System.out.println("Using Destination: " + destination + " Event: " + event);
            }

            File file = getFileToUpload();

            Map<String, String> metadata = DestinationsUtil.getMetadata(destination, event, file);

            uploadFile(baseUrl, file, metadata, token, tusUploadExecutor, null);
        }
    }

    private static void uploadFile(String baseUrl, File file, Map<String, String> metadata, String token,
        TusUploadExecutor tusUploadExecutor, Upload upload) throws Exception {     
    
        tusUploadExecutor.initiateUpload(token, baseUrl, file, metadata);

        String tguid = tusUploadExecutor.getTguid();
        System.out.println("TGUID Received: " + tguid);
        if(upload != null) { upload.setTguid(tguid); }

        System.out.println("Checking file Status");
        FileStatus fileStatus = StatusUtil.getFileStatus(token, tguid, baseUrl);

        String status = fileStatus.getStatus();

        if(status.equalsIgnoreCase("Complete")) {

            System.out.println("File Uploaded Successfully!");
            if(upload != null) { upload.setUploadStatus("SUCCESS"); }
        }
    }

    private static void printValues(String username, String password, String baseUrl) {        
        System.out.println("username: " + username);

        if(password != null && !password.isEmpty()) {
            System.out.println("password: *****");
        } else {
            System.out.println("password: null");
        }
        
        System.out.println("baseUrl: " + baseUrl);
    }

    private static File getFileToUpload() throws Exception {        
        try { 
            File tempFile =  File.createTempFile("test-upload-file", ".temp");
            tempFile.deleteOnExit();

            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write("--empty file--".getBytes());
            fos.close();

            return tempFile;
        } catch (Exception e) {
            System.out.println("Failed to create test upload file!");
            throw e;
        }
    }    
}
