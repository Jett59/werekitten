package com.mycodefu.werekitten.pipeline;

import com.mycodefu.werekitten.Start;
import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.NetworkEventType;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Pipeline {
    private final Queue<PipelineEvent> eventQueue;
    private final Map<Event, PipelineHandler[]> eventHandlers;
    private final PipelineHandler[] handlers;
    private final int eventsToRunPerFrame;
    private String name;
    private final PipelineContext context;

    public Pipeline(String name, PipelineContext context, int eventsToRunPerFrame, PipelineHandler... handlers) {
        this.name = name;
        this.context = context;
        if (handlers == null || handlers.length == 0) {
            throw new IllegalArgumentException("Handlers may not be null or empty.");
        } else {
            Map<Event, List<PipelineHandler>> eventHandlersList = new HashMap<>();
            for (PipelineHandler handler : handlers) {
                if (handler == null) {
                    throw new IllegalArgumentException("No handler in the handlers list may be null.");
                }
                Event[] handlerEvents = handler.getEventInterest();
                for (Event handlerEvent : handlerEvents) {
                    if (!eventHandlersList.containsKey(handlerEvent)) {
                        eventHandlersList.put(handlerEvent, new ArrayList<>());
                    }
                    eventHandlersList.get(handlerEvent).add(handler);
                }
            }
            this.handlers = handlers;
            this.eventHandlers = new HashMap<>();
            for (Map.Entry<Event, List<PipelineHandler>> eventListEntry : eventHandlersList.entrySet()) {
                this.eventHandlers.put(eventListEntry.getKey(), eventListEntry.getValue().toArray(new PipelineHandler[]{}));
            }
        }
        if (eventsToRunPerFrame < 1 || eventsToRunPerFrame > 10) {
            throw new IllegalArgumentException("eventsToRunPerFrame must be between 1 and 10.");
        }
        this.eventsToRunPerFrame = eventsToRunPerFrame;
        this.eventQueue = new ConcurrentLinkedQueue<>();
    }

    public String getName() {
        return name;
    }

    public void processEvents() {
        for (int n = 0; n < eventsToRunPerFrame; n++) {
            //get the PipelineEvent from the front of the eventQueue
            PipelineEvent event = eventQueue.poll();

            //check if the event is null, if it is, that means that the queue is empty, exit early
            if (event == null) {
                return;
            }
            if(Start.DEBUG_PIPELINE_EVENTS) {
                System.out.println("rendering event "+event.getEvent().getName());
            }
            PipelineHandler[] handlers = this.eventHandlers.get(event.getEvent());
            if (handlers != null) {
                for (PipelineHandler handler : handlers) {
                    handler.handleEvent(context, event);
                }
            }
        }
    }

    public void addEvent(PipelineEvent event) {
        eventQueue.offer(event);
    }

    @Override
    public String toString() {
        return "Pipeline{" +
                "name='" + name + '\'' +
                ", handlers.length=" + handlers.length +
                '}';
    }
}
