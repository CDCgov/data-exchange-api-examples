import io.github.cdimascio.dotenv.Dotenv;
import io.tus.java.client.TusClient;
import io.tus.java.client.TusExecutor;
import io.tus.java.client.TusUploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.URL;

import static org.mockito.Mockito.*;

class TusUploaderTest {
    private Dotenv dotenv;
    private TusClient client;
    private TusUploader uploader;

    @BeforeEach
    void setUp() {
        dotenv = mock(Dotenv.class);
        client = mock(TusClient.class);
        uploader = new TusUploader(dotenv, client);
    }

    @Test
    void makeUploadTest() throws Exception {
        // Set up the mock behavior
        when(dotenv.get("AUTH_TOKEN")).thenReturn("dummy_token");
        when(dotenv.get("DEX_URL")).thenReturn("https://example.com");

        TusExecutor executor = mock(TusExecutor.class);
        when(client.resumeOrCreateUpload(any())).thenReturn(uploader);
        when(uploader.getOffset()).thenReturn(0L, 512L, 1024L);
        when(uploader.uploadChunk()).thenReturn(512, 512, -1);
        when(uploader.getUploadURL()).thenReturn(new URL("https://example.com/upload"));

        whenNew(TusExecutor.class).withNoArguments().thenReturn(executor);

        uploader.makeUpload();
    }
}
