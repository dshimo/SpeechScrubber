package com.speechscrubber.parser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;
import com.ibm.json.java.OrderedJSONObject;

public class TimeStampJSONParser {
	
	private static List<Double> times = new ArrayList<Double>();

	TimeStampJSONParser(JSONObject transcript) {

	   OrderedJSONObject jsonObject = (OrderedJSONObject) transcript;
	   
	   JSONArray monologues = (JSONArray) jsonObject.get("monologues");
	   
	   JSONObject speaker1 = (JSONObject) monologues.get(1);
	   
	   JSONArray elements = (JSONArray) speaker1.get("elements");
	   
	   String sentence = "monologues are block of text";
	   String[] sentenceArray = sentence.split(" ");
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
					   System.out.println("Time is null: " + time);
					   break;
					   
				    } 
				   else {
				    		System.out.println(value + " == " + sentenceArray[i].equals(value));
				    		if(sentenceArray.length == (i + 1)) {
				    		    System.out.println("count= " + count);
				    		}
				    		break;
				    	
				    }
				   
				}
		   		else if (!sentenceArray[i].equals(value)) {
		   			System.out.println(sentenceArray[i] + " != " + value);
		   			if(time != null) {	
		   				if(elements.size() == (j + 1)) {
		   					System.out.println("elements.size() = " + (j + 1));
		   					System.out.println("count= " + count);
		   					if( (count + 1) == times.size()) {
		   						System.out.println("Times is == to size");
		   						times.remove(count);
		   						time = null;
		   						if(times.contains(count))
		   							System.out.println("Time countains count");
		   						System.out.println("count= " + count);
		   						done = true;
		   					}
		   					else if (count > times.size()) {
		   						System.out.println("Count < size");
		   						times.remove(count);
		   					}
		   				}
		   			}
		   			else if (time == null && elements.size() == (j + 1) && i == 0) {
		   				System.out.println("Count < size" + time + elements.size() );
		   			    done = true;
		   			}
			   }
		   }
		   if(done == true)
			   break;
		   if(time != null) {
			   if(times.isEmpty()) {
				   System.out.println("adding time to times");
				   times.add(count, time);
			   }
			   if(times.get(count) < time) {
				   System.out.println("Not adding time to times");
				   time = null;
			   }	   			   
		   }	  
		   
		   
	      }
		  System.out.println("Amount times string was found: " + times.size());
		  System.out.println("Amount times string was found: " + times.get(count));
	  } else {
		  System.out.println("Did not find string");
	  }
	}

	public static List<Double> getTimeStamps() {
		return times;
	}
}

