import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.UUID;

import static org.mockito.Mockito.*;

class TusUploaderTest {
    
    @Test
    void tusUploaderTest() throws Exception {
        // Mock the dependencies
        Dotenv dotenv = mock(Dotenv.class);
        TusClient client = mock(TusClient.class);
        
        // Set up the mock behavior
        when(dotenv.get("AUTH_TOKEN")).thenReturn("dummy_token");
        when(dotenv.get(anyString())).thenReturn("dummy_value");
        when(client.upload(Mockito.any(), Mockito.anyBoolean())).thenReturn(mock(TusUploader.class));
        
        TusUploader uploader = new TusUploader(dotenv, client);
        uploader.TusUploader(); // Call the method
        
        // Add assertions to verify the behavior and results
        // ...
    }
}
