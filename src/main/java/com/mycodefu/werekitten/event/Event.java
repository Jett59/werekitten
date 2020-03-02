package com.mycodefu.werekitten.event;

public interface Event {
String getName();

public static Event[] combineEvents(Event[]... events) {
	int totalLength = 0;
	for(Event[] eventArray : events) {
		totalLength+=eventArray.length;
	}
	Event[] result = new Event[totalLength];
	int totalIndex = 0;
	for(Event[] eventArray : events) {
		for(Event event : eventArray) {
			result[totalIndex] = event;
			totalIndex++;
		}
	}
	return result;
}
public static Event[] combineEvents(Event[] array, Event... events) {
	return combineEvents(array, events);
}
}
