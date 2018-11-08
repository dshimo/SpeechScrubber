package com.speechscrubber.job;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.speechscrubber.Constants;
import com.speechscrubber.web.HttpUtils;

public class JobPoster {

    private HttpUtils httpUtils = new HttpUtils();

    public String postAudio(HttpServletRequest request) {
        System.out.println("(JobPoster) Posting audio...");
        StringBuilder builder = new StringBuilder();
        try {
            System.out.println("Getting auth header...");
            Map<String, String> requestHeaders = httpUtils.getAuthorizationHeader();
            System.out.println("Creating connection...");
            HttpURLConnection connection = httpUtils.createHttpUrlConnection(Constants.JOBS_URL, requestHeaders, "POST");
            //            connection.setRequestProperty("Content-Type", "multipart/form-data");
            connection.setRequestProperty("Content-Type", "application/json");

            String fileName = request.getParameter("filename");
            System.out.println("Got file name: " + fileName + "...");

            System.out.println("Getting output stream...");
            OutputStream output = connection.getOutputStream();

            System.out.println("Setting up writer...");
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, "UTF-8"), true);

            Map<String, String> env = System.getenv();
            for (Entry<String, String> entry : env.entrySet()) {
                System.out.println("(Env) " + entry.getKey() + "=[" + entry.getValue() + "]");
            }

            writer.append("\r\n");

            String body = "{\"media_url\": \"https://support.rev.com/hc/en-us/article_attachments/200043975/FTC_Sample_1_-_Single.mp3\",\"metadata\": \"AYOHO test\""/*
                                                                                                                                                                      * ,\"callback_url\": \"
                                                                                                                                                                      * https
                                                                                                                                                                      * :
                                                                                                                                                                      * /
                                                                                                                                                                      * /
                                                                                                                                                                      * www
                                                                                                                                                                      * .
                                                                                                                                                                      * example
                                                                                                                                                                      * .
                                                                                                                                                                      * com
                                                                                                                                                                      * /
                                                                                                                                                                      * callback\
                                                                                                                                                                      */ + "}";
            writer.append(body).append("\r\n");

            //            String paramMedia = "media=@" + env.get("PWD") + "/" + fileName + ";type=audio/*";
            //            //            String paramMedia = "media=@https://support.rev.com/hc/en-us/article_attachments/200043975/FTC_Sample_1_-_Single.mp3;type=audio/*";
            //            System.out.println("Writing: [" + paramMedia + "]");
            //            writer.append(paramMedia).append("\r\n");
            //            //            output.write((paramMedia + "\r\n").getBytes("UTF-8"));
            //
            //            String paramOptions = "options={\"metadata\":\"AYOHO test\"}";
            //            System.out.println("Writing: [" + paramOptions + "]");
            //            writer.append(paramOptions).append("\r\n");
            //            //            output.write((paramOptions + "\r\n").getBytes("UTF-8"));

            writer.flush();

            //            output.write(("media=@" + env.get("PWD") + "/TOTC.m4a;type=audio/*").getBytes("UTF-8"));
            //            output.write(("options={\"metadata\":\"AYOHO test\"}").getBytes("UTF-8"));

            //            System.out.println("Getting input stream...");
            //            InputStream inStream = request.getInputStream();
            //            byte[] buffer = new byte[1024];
            //            int totalBytes = 0;
            //            int bytesRead = 0;
            //            System.out.println("Starting loop...");
            //            while ((bytesRead = inStream.read(buffer)) > -1) {
            //                output.write(buffer);
            //                System.out.println("\tRead [" + bytesRead + "] bytes");
            //                totalBytes += bytesRead;
            //            }
            //            System.out.println("Posting total bytes: " + totalBytes);
            //            connection.setFixedLengthStreamingMode(totalBytes);

            System.out.println("Reading connection response...");
            builder.append(httpUtils.readConnectionResponse(connection));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Caught exception: " + e);
            builder.append("Failed to post audio: " + e);
        }

        return builder.toString();
    }

    private void test() {

    }
}
