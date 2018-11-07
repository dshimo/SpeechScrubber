package com.speechscrubber.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtils {

    public String sendGetRequest(String targetUrl) {
        return sendGetRequest(targetUrl, null);
    }

    public String sendGetRequest(String targetUrl, Map<String, String> requestProperties) {
        HttpURLConnection connection = null;
        try {
            connection = createHttpUrlConnection(targetUrl, requestProperties);
            return readConnectionResponse(connection);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    HttpURLConnection createHttpUrlConnection(String targetUrl, Map<String, String> requestProperties) throws MalformedURLException, IOException, ProtocolException {
        URL url = new URL(targetUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setUseCaches(false);
        connection.setDoOutput(false);
        if (requestProperties != null) {
            for (Entry<String, String> entry : requestProperties.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        //        connection.setRequestProperty("Authorization", "Bearer " + getApiKey());
        return connection;
    }

    String readConnectionResponse(HttpURLConnection connection) throws IOException {
        InputStream is = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }

}
