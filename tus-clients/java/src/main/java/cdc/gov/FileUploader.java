package cdc.gov;

import java.io.File;
import java.net.URL;
import java.util.Properties;

import cdc.gov.model.FileStatus;
import cdc.gov.utils.LoginUtil;
import cdc.gov.utils.StatusUtil;

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

            System.out.println("Getting Token");
            String token = LoginUtil.getToken(username, password, baseUrl);

            TusUploadExecutor tusUploadExecutor = new TusUploadExecutor();

            tusUploadExecutor.initiateUpload(token, baseUrl, getFileToUpload("1MB-test-file"));

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
}
