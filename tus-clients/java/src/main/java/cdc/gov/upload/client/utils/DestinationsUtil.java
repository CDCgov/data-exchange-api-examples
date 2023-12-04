package cdc.gov.upload.client.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DestinationsUtil {
    
    public static Map<String, String> getMetadata(String destination, String event, File file) throws Exception {

        HashMap<String, String> metadataMap = new HashMap<>();

        if(destination.equalsIgnoreCase("dextesting") &&  event.equalsIgnoreCase("testevent1")) {

            metadataMap.put("meta_destination_id", destination);
            metadataMap.put("meta_ext_event", event);        
            metadataMap.put("filename", file.getName());
            metadataMap.put("meta_ext_source", "INTEGRATION-TEST");

        } else {

            throw new Exception("No metadta found for Destination: " + destination + " Event: " + event);
        }

        return metadataMap;
    }
}
