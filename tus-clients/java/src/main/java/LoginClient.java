import netscape.javascript.JSObject;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LoginClient {
    public static String login(String username, String password, String url) {
        try {
            URL apiUrl = new URL(url + "oauth");
            HttpURLConnection conn = (HttpURLConnection) apiUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Build the request body
            String requestBody = "username=" + username + "&password=" + password;
            byte[] postData = requestBody.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;

            // Set request headers
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataLength));

            // Send the request
            try (OutputStream os = conn.getOutputStream()) {
                os.write(postData);
            }

            // Get the response
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }

                    JSONObject jsonResponse = new JSONObject(response.toString());
                    String accessToken = jsonResponse.getString("access_token");
                    System.out.println("accessToken-----." +accessToken);
                    return accessToken;
                }
            } else {
                System.err.println("Client login failed to SAMS, error code is " + responseCode +
                        ", error message is " + conn.getResponseMessage());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
