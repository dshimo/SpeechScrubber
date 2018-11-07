package com.speechscrubber.job;

import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

import com.speechscrubber.Constants;

public class JobChecker {

    public static final String JOBS_URL = Constants.BASE_URL + "/jobs/";
    public static final String KEY_STATUS = "status";
    public static final String STATUS_TRANSCRIBED = "transcribed";

    public String getApiKey() {
        return System.getProperty(Constants.API_KEY_SYSTEM_PROP);
    }

    public boolean isTranscriptReady(String jobId) {
        JsonObject jobData = getJobData(jobId);
        try {
            return isJobTranscribed(jobData);
        } catch (JobException e) {
            e.printStackTrace();
        }
        return false;
    }

    JsonObject getJobData(String jobId) {
        // TODO
        throw new RuntimeException("Not implemented yet");
    }

    boolean isJobTranscribed(JsonObject jobData) throws JobException {
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
        String status = jobData.getString(KEY_STATUS);
        if (status.equals(STATUS_TRANSCRIBED)) {
            return true;
        }
        return false;
    }

}
