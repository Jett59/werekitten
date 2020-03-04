package com.mycodefu.werekitten.event;

public interface Event {
String getName();

public static Event[] combineEvents(Event[]... events) {
	System.out.println("combining arrays");
	int totalLength = 0;
	for(Event[] eventArray : events) {
		totalLength+=eventArray.length;
	}
	System.out.println("array to be combined has a length of "+totalLength);
	Event[] result = new Event[totalLength];
	int totalIndex = 0;
	for(Event[] eventArray : events) {
		for(Event event : eventArray) {
			try {
			result[totalIndex] = event;
			}catch(Exception e) {
				System.out.println("exception caught in the event contruction process");
				e.printStackTrace();
			}
			totalIndex++;
		}
	}
	return result;
}
public static Event[] combineEvents(Event[] array, Event event) {
	//System.out.println("combining events");
	try {
	return combineEvents(array, new Event[] {event});
	}catch (Exception e) {
		//throw new RuntimeException(String.format("exception thrown %s in event combination process", e.toString()));
		return null;
	}
}
}
