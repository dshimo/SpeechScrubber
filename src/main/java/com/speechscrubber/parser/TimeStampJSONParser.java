package com.speechscrubber.parser;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class TimeStampJSONParser {

    private static List<Double> times = new ArrayList<Double>();

    public TimeStampJSONParser(JsonObject transcript, String searchString) {

        JsonObject jsonObject = transcript;

        if (jsonObject == null)
            return;

        JsonArray monologues = jsonObject.getJsonArray("monologues");
        System.out.println("Monologues: " + monologues);

        if (monologues == null)
            return;

        // TODO: Handle more than one speaker
        JsonObject speaker = (JsonObject) monologues.get(0);
        System.out.println("speaker: " + speaker);

        if (speaker == null)
            return;

        JsonArray elements = speaker.getJsonArray("elements");
        System.out.println("elements: " + elements);

        if (elements == null)
            return;

        String[] sentenceArray = searchString.split(" ");
        Double time = null;
        boolean done = false;
        int count = 0;

        System.out.println();
        if (elements.size() > sentenceArray.length) {
            for (int i = 0; i < sentenceArray.length; i++) {
                for (int j = 0; j < elements.size(); j++) {
                    JsonObject element = (JsonObject) elements.get(j);
                    System.out.println("Checking element: " + element);
                    String type = element.getString("type");
                    System.out.println("\ttype: " + type);

                    String value = element.getString("value");
                    System.out.println("\tvalue: " + value);

                    if (sentenceArray[i].equals(value)) {
                        System.out.println("\tFound matching word");
                        if (time == null) {
                            System.out.println("\t\tAssociated time: " + time);

                            time = element.getJsonNumber("ts").doubleValue();
                            break;

                        } else {
                            System.out.println("\t\tNada");
                            break;

                        }

                    } else if (!sentenceArray[i].equals(value)) {

                        System.out.println("\tNo match");
                        if (time != null) {
                            if (elements.size() == (j + 1)) {
                                if ((count + 1) == times.size()) {
                                    System.out.println("\t\t(1) Removing count [" + count + "] from times");
                                    times.remove(count);
                                    time = null;
                                    done = true;
                                } else if (count > times.size()) {
                                    System.out.println("\t\t(2) Removing count [" + count + "] from times");
                                    times.remove(count);
                                }
                            }
                        } else if (time == null && elements.size() == (j + 1) && i == 0) {
                            System.out.println("\tDone! Right...?");
                            done = true;
                        }
                    }
                }
                if (done == true)
                    break;
                if (time != null) {
                    if (times.isEmpty()) {
                        System.out.println("Times is empty, so adding stuff...");
                        times.add(count, time);
                    }
                    System.out.println("Checking times");
                    if (times.get(count) < time) {
                        time = null;
                    }
                }

            }
        } else {
            System.out.println("No man's land");
        }
    }

    public static List<Double> getTimeStamps() {
        return times;
    }
}
