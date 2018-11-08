package com.speechscrubber.job;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

import com.speechscrubber.Constants;
import com.speechscrubber.web.HttpUtils;

public class JobChecker {

    public static final String JOBS_URL = Constants.BASE_URL + "/jobs/";
    public static final String KEY_STATUS = "status";
    public static final String KEY_ID = "id";
    public static final String STATUS_TRANSCRIBED = "transcribed";

    private HttpUtils httpUtils = new HttpUtils();

    public String getApiKey() {
        return System.getenv(Constants.API_KEY_SYSTEM_PROP);
    }

    public boolean isTranscriptReady(String jobId) {
        try {
            verifyJobIdFormat(jobId);
            JsonObject jobData = getJobData(jobId);
            return isJobTranscribed(jobData);
        } catch (JobException e) {
            e.printStackTrace();
        }
        return false;
    }

    void verifyJobIdFormat(String jobId) throws JobException {
        if (jobId == null) {
            throw new JobException("The provided job ID is not considered value because it is null.");
        }
        if (jobId.isEmpty()) {
            throw new JobException("The provided job ID is not considered value because it is empty.");
        }
        if (!Pattern.matches("^[0-9]+$", jobId)) {
            throw new JobException("The provided job ID is not considered value because it does not consist of only numeric characters. Job ID was [" + jobId + "].");
        }
    }

    JsonObject getJobData(String jobId) throws JobException {
        Map<String, String> requestHeaders = createRequestHeaders();
        String response = httpUtils.sendGetRequest(JOBS_URL + jobId, requestHeaders);
        return convertResponseToJson(response);
    }

    Map<String, String> createRequestHeaders() {
        Map<String, String> requestProperties = new HashMap<String, String>();
        requestProperties.put("Authorization", "Bearer " + getApiKey());
        return requestProperties;
    }

    JsonObject convertResponseToJson(String response) throws JobException {
        if (response == null) {
            throw new JobException("Cannot convert response to JSON because the response is null.");
        }
        try {
            return Json.createReader(new StringReader(response)).readObject();
        } catch (Exception e) {
            throw new JobException("Failed to convert response to JSON. Response was [" + response + "]. Exception was: [" + e + "]");
        }
    }

    boolean isJobTranscribed(JsonObject jobData) throws JobException {
        String status = getStatus(jobData);
        return status.equals(STATUS_TRANSCRIBED);
    }

    String getStatus(JsonObject jobData) throws JobException {
        if (jobData == null) {
            throw new JobException("No job data has been provided; the provided job data is null.");
        }
        if (!jobData.containsKey(KEY_STATUS)) {
            throw new JobException("The provided job data does not include a status. Job data was: " + jobData);
        }
        JsonValue rawStatus = jobData.get(KEY_STATUS);
        if (rawStatus.getValueType() != ValueType.STRING) {
            throw new JobException("The status value [" + rawStatus + "] was not a string as expected.");
        }
        return jobData.getString(KEY_STATUS);
    }

}
