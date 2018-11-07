import javax.json.JsonObject;

public class JobChecker {

    public static final String JOBS_URL = Constants.BASE_URL + "/jobs/";

    public String getApiKey() {
        return System.getProperty(API_KEY_SYSTEM_PROP);
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

    private JsonObject getJobData(String jobId) {
        // TODO
        throw new RuntimeException("Not implemented yet");
    }

    private boolean isJobTranscribed(JsonObject jobData) throws JobException {
        if (jobData == null) {
            throw new JobException("No job data has been provided; the provided job data is null.");
        }
        if (!jobData.containsKey("status")) {
            throw new JobException("The provided job data does not include a status. Job data was: " + jobData);
        }
        // TODO
        throw new RuntimeException("Not implemented yet");
    }

}
