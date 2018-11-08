package com.speechscrubber.parser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import  javax.json.JsonObject;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;
import com.ibm.json.java.OrderedJSONObject;

public class TimeStampJSONParser {
	
	private static List<Double> times = new ArrayList<Double>();

	public TimeStampJSONParser(JsonObject transcript, String searchString) {

	   OrderedJSONObject jsonObject = (OrderedJSONObject) transcript;
	   
	   JSONArray monologues = (JSONArray) jsonObject.get("monologues");
	   
	   JSONObject speaker1 = (JSONObject) monologues.get(1);
	   
	   JSONArray elements = (JSONArray) speaker1.get("elements");

	   String[] sentenceArray = searchString.split(" ");
	   Double time = null;
	   boolean done = false;
	   int count = 0;

	   if(elements.size() > sentenceArray.length) {
	   for(int i = 0; i < sentenceArray.length; i++) {
		   for(int j = 0; j < elements.size(); j++) {
			   JSONObject element = (JSONObject) elements.get(j);
			   String type = (String) element.get("type");
					   
			   String value = (String) element.get("value");
				   
			   if(sentenceArray[i].equals(value)) {
				   if(time == null) {
					   
					   time = Double.valueOf((Double) element.get("ts"));
					   break;
					   
				    } 
				   else {
				    		break;
				    	
				    }
				   
				}
		   		else if (!sentenceArray[i].equals(value)) {

		   			if(time != null) {	
		   				if(elements.size() == (j + 1)) {
		   					if( (count + 1) == times.size()) {
		   						times.remove(count);
		   						time = null;
		   						done = true;
		   					}
		   					else if (count > times.size()) {
		   						times.remove(count);
		   					}
		   				}
		   			}
		   			else if (time == null && elements.size() == (j + 1) && i == 0) {
		   			    done = true;
		   			}
			   }
		   }
		   if(done == true)
			   break;
		   if(time != null) {
			   if(times.isEmpty()) {
				   times.add(count, time);
			   }
			   if(times.get(count) < time) {
				   time = null;
			   }	   			   
		   }	  
		   
	   }   
	  } else {  
	  }
	}

	public static List<Double> getTimeStamps() {
		return times;
	}
}

