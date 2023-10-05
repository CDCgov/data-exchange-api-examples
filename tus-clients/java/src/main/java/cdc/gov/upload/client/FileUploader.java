package cdc.gov.upload.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

import cdc.gov.upload.client.model.*;
import cdc.gov.upload.client.tus.TusUploadExecutor;
import cdc.gov.upload.client.utils.LoginUtil;
import cdc.gov.upload.client.utils.ReadJsonFromAzureStorage;
import cdc.gov.upload.client.utils.StatusUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FileUploader {

    public static void main(String[] args) {

        String destination_id = null;
        String event_name = null;
        String metaDataDefinition = null;
        String definitionFilename = null;
        Map<String, String> metadata = new HashMap<>();

        ObjectMapper objectMapper = new ObjectMapper();
        try {

            Properties prop = new Properties();
            prop.load(LoginUtil.class.getClassLoader().getResourceAsStream("env.properties"));

            String username = System.getProperty("USERNAME");
            if (username == null || username.isEmpty()) {
                username = prop.getProperty("USERNAME");
            }

            String password = System.getProperty("PASSWORD");
            if (password == null || password.isEmpty()) {
                password = prop.getProperty("PASSWORD");
            }

            String baseUrl = System.getProperty("URL");
            if (baseUrl == null || baseUrl.isEmpty()) {
                baseUrl = prop.getProperty("URL");
            }

            System.out.println("username: " + username);
            if (password != null && !password.isEmpty()) {
                System.out.println("password: *****");
            } else {
                System.out.println("password: " + password);
            }

            System.out.println("baseUrl: " + baseUrl);

            System.out.println("Getting Token");
            String token = LoginUtil.getToken(username, password, baseUrl);

            TusUploadExecutor tusUploadExecutor = new TusUploadExecutor();

            File file = getFileToUpload("1MB-test-file");

            //InputStream inputStream = JsonParser.class.getResourceAsStream("/allowed_destination_and_events.json");
            String jsonContent = ReadJsonFromAzureStorage.getAzureStorage();
            List<Destination> destinations = objectMapper.readValue(jsonContent, new TypeReference<List<Destination>>() {
            });
            System.out.println("arg: " + (args.length > 0));
            if (args.length > 0) {
                if (args[0] != null && args[0].contains("-")) {
                    System.out.println("Destination :" + args[0]);
                    String[] input = args[0].split("-");
                    destination_id = input[0].trim();
                    event_name = input[1].trim();
                    List<ExtEvent> extEvents = null;
                    for (Destination destination : destinations) {

                        if (destination_id.equals(destination.getDestination_id().trim())) {
                            extEvents = destination.getExt_events();
                            break;
                        }
                     }

                        for (ExtEvent extEvent : extEvents) {
                            System.out.println("extEvent.getName().trim(): " + extEvent.getName().trim());
                            if (event_name.equals(extEvent.getName().trim())) {
                                definitionFilename = extEvent.getDefinition_filename();
                                metaDataDefinition = ReadJsonFromAzureStorage.getMetaDataDefinition(definitionFilename);
                                System.out.println("in  destination_id: " + destination_id);
                                System.out.println("in event_name: " + destination_id);
                                break;
                            }

                    }

                    System.out.println("out definitionFilename: " + definitionFilename);
                    System.out.println("out metaDataDefinition: " + metaDataDefinition);
                    metadata = getMetadata(destination_id, event_name, metaDataDefinition, file);
                    tusUploadExecutor.initiateUpload(token, baseUrl, file, metadata);

                    String tguid = tusUploadExecutor.getTguid();
                    System.out.println("TGUID Received: " + tguid);

                    System.out.println("Checking file Status");
                    FileStatus fileStatus = StatusUtil.getFileStatus(token, tguid, baseUrl);
                    System.out.println("fileStatus" + fileStatus);
                    String status = fileStatus.getStatus();
                    if (status.equalsIgnoreCase("Complete")) {

                        System.out.println("File Uploaded Successfully!");
                    }

                }
            } else{

            for (Destination destination : destinations) {
                destination_id = destination.getDestination_id().trim();
                System.out.println("destination_id Received: " + destination_id);
                for (ExtEvent extEvent : destination.getExt_events()) {
                    event_name = extEvent.getName().trim();
                    definitionFilename = extEvent.getDefinition_filename();
                    System.out.println("event_name: " + event_name);
                    metaDataDefinition = ReadJsonFromAzureStorage.getMetaDataDefinition(definitionFilename);
                    metadata = getMetadata(destination_id, event_name, metaDataDefinition, file);

                    tusUploadExecutor.initiateUpload(token, baseUrl, file, metadata);

                    String tguid = tusUploadExecutor.getTguid();
                    System.out.println("TGUID Received: " + tguid);

                    System.out.println("Checking file Status");
                    FileStatus fileStatus = StatusUtil.getFileStatus(token, tguid, baseUrl);

                    String status = fileStatus.getStatus();

                    if (status.equalsIgnoreCase("Complete")) {

                        System.out.println("File Uploaded Successfully!");
                    }
                    System.out.println("======End of " + destination_id + "-" + event_name + "  destinations=========");
                }
                System.out.println("======End of " + destination_id + "  destinations=========");
            }
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

    private static Map<String, String> getMetadata(String destination, String event,String definitionFilename, File file) throws JsonProcessingException {

        HashMap<String, String> metadataMap = new HashMap<>();

        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("definitionFilename-----" + definitionFilename);

        List<Schema> schemaLists = objectMapper.readValue(definitionFilename, new TypeReference<List<Schema>>() {});
        for(Schema schema: schemaLists) {
            List<Field> fields = schema.getFields();
            for (Field field : fields) {
                String fieldName = field.getFieldname();
                String required = field.getRequired();
                List<String> allowedValues = (List<String>)field.getAllowed_values();

                if (allowedValues != null ) {
                    //String[] split = allowedValues.toString().split(",");
                   // String selectedAllowedValues = split[0].substring(2, split[0].length() - 1);
                    metadataMap.put(fieldName, allowedValues.get(0));
                } else {
                    metadataMap.put(fieldName, " ");
                }

            }
        }

        metadataMap.put("meta_destination_id", destination);
        metadataMap.put("meta_ext_event", event);
        metadataMap.put("filename", file.getName());
        metadataMap.put("original_filename", file.getName());
        metadataMap.put("filetype", "text/plain");
        metadataMap.put("meta_username", "ygj6@cdc.gov");
        metadataMap.put("meta_ext_filename", file.getName());
        metadataMap.put("meta_ext_objectkey", UUID.randomUUID().toString());
        metadataMap.put("meta_ext_file_timestamp", String.valueOf(file.lastModified()));
        metadataMap.put("original_file_timestamp", String.valueOf(file.lastModified()));


        return metadataMap;
    }    
}
