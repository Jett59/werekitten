package com.mycodefu.werekitten.event;

public interface Event {
    String getName();

    static Event[] combineEvents(Event[]... events) {
        int totalLength = 0;
        for (Event[] eventArray : events) {
            totalLength += eventArray.length;
        }
        Event[] result = new Event[totalLength];
        int totalIndex = 0;
        for (Event[] eventArray : events) {
            for (Event event : eventArray) {
                try {
                    result[totalIndex] = event;
                } catch (Exception e) {
                    System.out.println("exception caught in the event contruction process");
                    e.printStackTrace();
                }
                totalIndex++;
            }
        }
        return result;
    }

    static Event[] combineEvents(Event[] array, Event event) {
        return combineEvents(array, new Event[]{event});
    }

    static Event[] combineEvents(Event[] array, Event event1, Event event2) {
        return combineEvents(array, new Event[]{event1, event2});
    }

    static Event[] combineEvents(Event[] array, Event event1, Event event2, Event event3) {
        return combineEvents(array, new Event[]{event1, event2, event3});
    }
}
