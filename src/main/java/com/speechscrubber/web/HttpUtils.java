package com.speechscrubber.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.speechscrubber.Utils;

public class HttpUtils {

    public Map<String, String> getAuthorizationHeader() {
        Map<String, String> requestProperties = new HashMap<String, String>();
        requestProperties.put("Authorization", "Bearer " + Utils.getApiKey());
        return requestProperties;
    }

    public String sendGetRequest(String targetUrl) {
        return sendGetRequest(targetUrl, null);
    }

    public String sendGetRequest(String targetUrl, Map<String, String> requestProperties) {
        HttpURLConnection connection = null;
        try {
            connection = createHttpUrlConnection(targetUrl, requestProperties, "GET");
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

    public String sendPostRequest(String targetUrl, Map<String, String> requestProperties) {
        HttpURLConnection connection = null;
        try {
            connection = createHttpUrlConnection(targetUrl, requestProperties, "POST");
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

    public String sendPostRequest(String targetUrl) {
        return sendPostRequest(targetUrl, null);
    }

    public String sendPostRequest(String targetUrl, Map<String, String> requestProperties, Object body) {
        HttpURLConnection connection = null;
        try {
            connection = createHttpUrlConnection(targetUrl, requestProperties, "POST");
            connection.setDoOutput(true);
            // TODO - ayoho
            // See https://stackoverflow.com/questions/2793150/how-to-use-java-net-urlconnection-to-fire-and-handle-http-requests/2793153#2793153
            //            connection.setFixedLengthStreamingMode(contentLength);
            connection.setRequestProperty("Content-Type", "audio/mpeg");

            try (OutputStream output = connection.getOutputStream()) {
                //                output.write(query.getBytes(charset));
            }

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

    public HttpURLConnection createHttpUrlConnection(String targetUrl, Map<String, String> requestProperties, String requestType) throws MalformedURLException, IOException, ProtocolException {
        URL url = new URL(targetUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestType);
        connection.setUseCaches(false);
        if (requestType != null && "post".equalsIgnoreCase(requestType)) {
            connection.setDoOutput(true);
        }
        if (requestProperties != null) {
            for (Entry<String, String> entry : requestProperties.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        return connection;
    }

    public String readConnectionResponse(HttpURLConnection connection) throws IOException {
        int responseStatus = connection.getResponseCode();
        InputStream is;
        if (responseStatus > 299) {
            System.out.println("Got an error status (" + responseStatus + ")");
            is = connection.getErrorStream();
        } else {
            System.out.println("Got a successful status (" + responseStatus + ")");
            is = connection.getInputStream();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        System.out.println("Response: " + response.toString());
        return response.toString();
    }

}
