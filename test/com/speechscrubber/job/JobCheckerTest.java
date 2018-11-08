package com.speechscrubber.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.speechscrubber.Constants;

public class JobCheckerTest {

    JobChecker checker;
    String apiSysPropValue = null;

    @Before
    public void beforeTest() {
        checker = new JobChecker();
        apiSysPropValue = System.getProperty(Constants.API_KEY_SYSTEM_PROP);
    }

    @After
    public void afterTest() {
        if (apiSysPropValue != null) {
            System.setProperty(Constants.API_KEY_SYSTEM_PROP, apiSysPropValue);
        }
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

    @Test
    public void test_isTranscriptReady_validJob_transcribed() {
        String inputId = "85032187";
        boolean result = checker.isTranscriptReady(inputId);
        assertTrue("Job [" + inputId + "] should have its transcript ready, but evidently did not.", result);
    }

    /******************************************************* verifyJobIdFormat *******************************************************/

    @Test
    public void test_verifyJobIdFormat_nullId() {
        String jobId = null;
        try {
            checker.verifyJobIdFormat(jobId);
            fail("Should have thrown a JobException but did not.");
        } catch (JobException e) {
            // Expected
        }
    }

    @Test
    public void test_verifyJobIdFormat_emptyId() {
        String jobId = "";
        try {
            checker.verifyJobIdFormat(jobId);
            fail("Should have thrown a JobException but did not.");
        } catch (JobException e) {
            // Expected
        }
    }

    @Test
    public void test_verifyJobIdFormat_nonNumericId() {
        String jobId = "Testing123";
        try {
            checker.verifyJobIdFormat(jobId);
            fail("Should have thrown a JobException but did not.");
        } catch (JobException e) {
            // Expected
        }
    }

    @Test
    public void test_verifyJobIdFormat_numericIdWithWhitespace() {
        String jobId = "123 456 789";
        try {
            checker.verifyJobIdFormat(jobId);
            fail("Should have thrown a JobException but did not.");
        } catch (JobException e) {
            // Expected
        }
    }

    @Test
    public void test_verifyJobIdFormat_numericId() {
        String jobId = "123456789";
        try {
            checker.verifyJobIdFormat(jobId);
        } catch (JobException e) {
            fail("Encountered an unexpected exception: " + e);
        }
    }

    /******************************************************* getJobData *******************************************************/

    @Test
    public void test_getJobData_invalidJobId() {
        String inputId = "1";
        try {
            JsonObject result = checker.getJobData(inputId);
            fail("Expected to throw an exception but got result: " + result);
        } catch (JobException e) {
            // Expected
        }
    }

    @Test
    public void test_getJobData_validJobId() {
        String inputId = "85032187";
        try {
            JsonObject result = checker.getJobData(inputId);
            assertNotNull("Result should not have been null but was.", result);
            assertEquals("ID did not match expected value.", inputId, result.getString(JobChecker.KEY_ID));
            assertEquals("Status did not match expected value.", "transcribed", result.getString(JobChecker.KEY_STATUS));
        } catch (JobException e) {
            fail("Should not have thrown an exception but did. Exception was: " + e);
        }
    }

    /******************************************************* convertResponseToJson *******************************************************/

    @Test
    public void test_convertResponseToJson_nullResponse() {
        String response = null;
        try {
            JsonObject result = checker.convertResponseToJson(response);
            fail("Should have thrown an exception but didn't. Got result: " + result);
        } catch (JobException e) {
            // Expected
        }
    }

    @Test
    public void test_convertResponseToJson_emptyResponse() {
        String response = "";
        try {
            JsonObject result = checker.convertResponseToJson(response);
            fail("Should have thrown an exception but didn't. Got result: " + result);
        } catch (JobException e) {
            // Expected
        }
    }

    @Test
    public void test_convertResponseToJson_nonJsonResponse() {
        String response = "Some non-JSON value";
        try {
            JsonObject result = checker.convertResponseToJson(response);
            fail("Should have thrown an exception but didn't. Got result: " + result);
        } catch (JobException e) {
            // Expected
        }
    }

    @Test
    public void test_convertResponseToJson_emptyJson() {
        String response = Json.createObjectBuilder().build().toString();
        try {
            JsonObject result = checker.convertResponseToJson(response);
            assertTrue("Result should have been empty but was " + result, result.isEmpty());
        } catch (JobException e) {
            fail("Should not have thrown an exception but did. Exception was: " + e);
        }
    }

    @Test
    public void test_convertResponseToJson_nonEmptyJson() {
        String response = getSampleJsonObjectBuilder().build().toString();
        try {
            JsonObject result = checker.convertResponseToJson(response);
            assertFalse("Result should not have been empty but was.", result.isEmpty());
        } catch (JobException e) {
            fail("Should not have thrown an exception but did. Exception was: " + e);
        }
    }

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

    @Test
    public void test_isJobTranscribed_statusFailed() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(JobChecker.KEY_STATUS, "failed");
        JsonObject jobData = builder.build();
        try {
            boolean isTranscribed = checker.isJobTranscribed(jobData);
            assertFalse("Job should not have been considered transcribed but was. Job data was: " + jobData, isTranscribed);
        } catch (Exception e) {
            fail("Caught unexpected exception: " + e);
        }
    }

    @Test
    public void test_isJobTranscribed_statusTranscribed() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(JobChecker.KEY_STATUS, "transcribed");
        JsonObject jobData = builder.build();
        try {
            boolean isTranscribed = checker.isJobTranscribed(jobData);
            assertTrue("Job should have been considered transcribed but was not. Job data was: " + jobData, isTranscribed);
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
