package com.speechscrubber;

public class Utils {

    public static String getApiKey() {
        return System.getenv(Constants.API_KEY_SYSTEM_PROP);
    }

}
