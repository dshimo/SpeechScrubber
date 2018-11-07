package com.speechscrubber.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.junit.Before;
import org.junit.Test;

import com.speechscrubber.Constants;

public class JobCheckerTest {

    JobChecker checker;

    @Before
    public void beforeTest() {
        checker = new JobChecker();
    }

    /******************************************************* getApiKey *******************************************************/

    @Test
    public void test_getApiKey_keyNotSet() {
        System.clearProperty(Constants.API_KEY_SYSTEM_PROP);

        String key = checker.getApiKey();
        assertNull("API key was not null but should have been. Key was [" + key + "].", key);
    }

    @Test
    public void test_getApiKey_keySet() {
        String actualKey = "test";
        System.setProperty(Constants.API_KEY_SYSTEM_PROP, actualKey);

        String foundKey = checker.getApiKey();
        assertEquals("API key did not match expected value.", actualKey, foundKey);
    }

    /******************************************************* isTranscriptReady *******************************************************/

    /******************************************************* getJobData *******************************************************/

    /******************************************************* isJobTranscribed *******************************************************/

    @Test
    public void test_isJobTranscribed_nullData() {
        JsonObject jobData = null;
        try {
            boolean isTranscribed = checker.isJobTranscribed(jobData);
            fail("Should have thrown a JobException but did not. Was considered transcribed? " + isTranscribed);
        } catch (JobException e) {
            // Expected
        }
    }

    @Test
    public void test_isJobTranscribed_emptyData() {
        JsonObject jobData = Json.createObjectBuilder().build();
        try {
            boolean isTranscribed = checker.isJobTranscribed(jobData);
            fail("Should have thrown a JobException but did not. Was considered transcribed? " + isTranscribed);
        } catch (JobException e) {
            // Expected
        }
    }

    @Test
    public void test_isJobTranscribed_missingStatus() {
        JsonObject jobData = getSampleJsonObjectBuilder().build();
        try {
            boolean isTranscribed = checker.isJobTranscribed(jobData);
            fail("Should have thrown a JobException but did not. Was considered transcribed? " + isTranscribed);
        } catch (JobException e) {
            // Expected
        }
    }

    @Test
    public void test_isJobTranscribed_statusNotAString() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(JobChecker.KEY_STATUS, 3.14);
        JsonObject jobData = builder.build();
        try {
            boolean isTranscribed = checker.isJobTranscribed(jobData);
            fail("Should have thrown a JobException but did not. Was considered transcribed? " + isTranscribed);
        } catch (JobException e) {
            // Expected
        }
    }

    @Test
    public void test_isJobTranscribed_statusUnknown() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(JobChecker.KEY_STATUS, "unknown");
        JsonObject jobData = builder.build();
        try {
            boolean isTranscribed = checker.isJobTranscribed(jobData);
            assertFalse("Job should not have been considered transcribed but was. Job data was: " + jobData, isTranscribed);
        } catch (Exception e) {
            fail("Caught unexpected exception: " + e);
        }
    }

    public JsonObjectBuilder getSampleJsonObjectBuilder() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("string", "some string value");
        builder.add("number", 1);
        builder.add("boolean", true);
        builder.add("array", Json.createArrayBuilder().add(1).add(2));
        return builder;
    }

}
