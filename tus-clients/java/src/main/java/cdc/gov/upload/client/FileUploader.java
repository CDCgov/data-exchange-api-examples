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
            
            if(username != null && !username.isEmpty() &&
               password != null && !password.isEmpty() && 
               baseUrl  != null && !baseUrl.isEmpty()) {

                String destination = null, event = null;
                if(args.length == 2) {
                    destination = args[0];
                    event = args[1];
                }

                if((destination == null || destination.isEmpty()) && 
                    (event == null || event.isEmpty())) {
                    System.out.println("No Destination && Event Provided. Using Defaults: dextesting-testevent1");
                    destination = "dextesting";
                    event = "testevent1";
                } else {
                    System.out.println("Using Destination: " + destination + " Event: " + event);
                }

                System.out.println("Getting Token");
                String token = LoginUtil.getToken(username, password, baseUrl);

                TusUploadExecutor tusUploadExecutor = new TusUploadExecutor();

                File file = getFileToUpload("1MB-test-file");

                Map<String, String> metadata = getMetadata(destination, event, file);                
                
                tusUploadExecutor.initiateUpload(token, baseUrl, file, metadata);

                String tguid = tusUploadExecutor.getTguid();
                System.out.println("TGUID Received: " + tguid);
                        
                System.out.println("Checking file Status");
                FileStatus fileStatus = StatusUtil.getFileStatus(token, tguid, baseUrl);

                String status = fileStatus.getStatus();

                if(status.equalsIgnoreCase("Complete")) {
                    
                    System.out.println("File Uploaded Successfully!");
                }
            }
        } catch (Throwable t) {

            t.printStackTrace();
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

    private static File getFileToUpload(String fileName) throws Exception {        
        try {
            URL url = LoginUtil.class.getClassLoader().getResource(fileName);

            if(url != null) {
                File file = new File(url.getFile());

                if (file.exists() && !file.isDirectory()) {
                   return file;
                } else {
                    throw new Exception("upload file not found!");
                }
            } else {
                throw new Exception("upload file not found!");
            }                       
        } catch (Exception e) {
            throw e;
        }
    }

    private static Map<String, String> getMetadata(String destination, String event, File file) throws Exception {

        HashMap<String, String> metadataMap = new HashMap<>();

        if(destination.equalsIgnoreCase("dextesting") &&  event.equalsIgnoreCase("testevent1")) {

            metadataMap.put("meta_destination_id", destination);
            metadataMap.put("meta_ext_event", event);        
            metadataMap.put("filename", file.getName());
            metadataMap.put("filetype", "text/plain");
            metadataMap.put("meta_ext_source", "INTEGRATION-TEST");
            metadataMap.put("meta_ext_filename", file.getName());
            metadataMap.put("meta_ext_objectkey", UUID.randomUUID().toString());
            metadataMap.put("original_file_timestamp", String.valueOf(file.lastModified()));

        } else if(destination.equalsIgnoreCase("ndlp") &&  event.equalsIgnoreCase("routineImmunization")) {
        
            metadataMap.put("meta_destination_id", "ndlp");
            metadataMap.put("meta_ext_source", "IZGW");
            metadataMap.put("meta_ext_sourceversion", "V2022-12-31");
            metadataMap.put("meta_ext_event", "routineImmunization");           
            metadataMap.put("meta_ext_filename", "TEST-FILE-3.zip");
            metadataMap.put("meta_ext_submissionperiod", "2023Q1");
            metadataMap.put("meta_schema_version", "1.0");
            metadataMap.put("izgw_route_id", "dex-stg");
            metadataMap.put("izgw_ipaddress", "127.0.0.1");
            metadataMap.put("izgw_filesize", "1781578");
            metadataMap.put("izgw_path", "/upload/files/f394848234438a40878125e990adfdc7");
            metadataMap.put("izgw_uploaded_timestamp", "Mon, 21 Aug 2023 20:00:21 UTC");
            metadataMap.put("meta_ext_entity", "MAA");
            metadataMap.put("meta_username", "integration.testing.izgateway.org");
            metadataMap.put("meta_ext_objectkey", "5c70b304-9a07-3329-8cac-a64a5dcba380");

        } else {

            throw new Exception("No metadta found for Destination: " + destination + " Event: " + event);
        }

        return metadataMap;
    }    
}
