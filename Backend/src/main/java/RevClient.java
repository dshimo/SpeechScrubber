package main.java;

import java.io.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
public class RevClient {
    public final static void main(String[] args) {
    
        HttpClient httpClient = new DefaultHttpClient();
        try {
          HttpGet post = new HttpPost("https://api.rev.ai/revspeech/v1beta/jobs");
          HttpResponse httpResponse = httpClient.execute(httpGetRequest);
        }
        catch(Exception e) {

        }
      }
}

