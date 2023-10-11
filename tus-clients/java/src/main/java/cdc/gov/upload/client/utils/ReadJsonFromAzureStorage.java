package cdc.gov.upload.client.utils;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.BlobClient;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.ResourceBundle;

public class ReadJsonFromAzureStorage {


    public static String getConnectionString() {
        Properties props = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

            //load a properties file
            try(InputStream resourceStream = loader.getResourceAsStream("env.properties")) {
                props.load(resourceStream);
            }catch (IOException ex) {
                ex.printStackTrace();
            }
            //get the property value and print it out
            String accountName= props.getProperty("DEX_AZURE_STORAGE_ACCOUNT_NAME");
            String accountKey = props.getProperty("DEX_AZURE_STORAGE_ACCOUNT_KEY");
        if(accountName == null || accountKey == null)   {
            accountName = System.getenv("DEX_AZURE_STORAGE_ACCOUNT_NAME");
            accountKey  = System.getenv("DEX_AZURE_STORAGE_ACCOUNT_KEY");
        }

        String connectionString = "DefaultEndpointsProtocol=https;AccountName=" + accountName + ";AccountKey=" + accountKey + ";EndpointSuffix=core.windows.net";
        return connectionString;
    }

    public static boolean isSmokeTest() {
        Properties props = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        boolean smoke = false;
        //load a properties file
        try(InputStream resourceStream = loader.getResourceAsStream("env.properties")) {
            props.load(resourceStream);
        }catch (IOException ex) {
            ex.printStackTrace();
        }
        //get the property value and print it out
        String testMethod = props.getProperty("SMOKE");
        if(testMethod == null )   {
            testMethod = System.getenv("SMOKE");
        }
        if(testMethod.equals("true")){
            smoke = true;
        }

        return smoke;
    }



    public static String getAzureStorage() {
        String connectionString = getConnectionString();
        String containerName = "tusd-file-hooks";
        String blobName = "allowed_destination_and_events.json";
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blobClient.download(outputStream);
        String jsonContent = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);

        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonContent;
    }

    public static String getMetaDataDefinition(String definitionFilename) {

        String connectionString = getConnectionString();
        String containerName = "tusd-file-hooks";
        String blobName = definitionFilename;

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blobClient.download(outputStream);
        String jsonContent = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);


        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonContent;
    }
}