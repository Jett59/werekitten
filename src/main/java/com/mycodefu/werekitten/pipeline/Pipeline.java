package com.mycodefu.werekitten.pipeline;

import com.mycodefu.werekitten.Start;
import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.TimeEventType;
import com.mycodefu.werekitten.pipeline.events.time.FrameRateEvent;
import com.mycodefu.werekitten.pipeline.events.time.TickEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class Pipeline {
    private final Queue<PipelineEvent> eventQueue;
    private final Map<Event, PipelineHandler[]> eventHandlers;
    private final PipelineHandler[] handlers;
    private final int eventsToRunPerFrame;
    private String name;
    private final PipelineContext context;
    private long eventsInSecond = 0;
    private long nextEventFrameMillis = 0;

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

    /**
     * Call this method every frame of the game, from the game loop or timer.
     */
    public void tick() {
        monitorFrameRate();

        PipelineEvent event;
        while ((event = eventQueue.poll()) != null) {

            PipelineHandler[] handlers = this.eventHandlers.get(event.getEvent());
            
            if (Start.DEBUG_PIPELINE_EVENTS && !(event instanceof FrameRateEvent)) {
            	if(handlers != null) {
                System.out.printf("rendering event %s in the handlers %s\n", event.getEvent().getName(), Arrays.stream(handlers).map(pipeline->pipeline.getClass().getSimpleName()).collect(Collectors.toList()).toString());
            	}else {
            		System.out.printf("rendering event %s in no handlers\n", event.getEvent().getName());
            	}
            }
            
            if (handlers != null) {
                for (PipelineHandler handler : handlers) {
                    try {
                        handler.handleEvent(context, event);
                    } catch (Exception e) {
                        System.out.printf("An error occurred processing event '%s' in handler '%s':\n", event, handler.getClass().getSimpleName());
                        e.printStackTrace();
                    }
                }
            }
        }

        PipelineHandler[] tickHandlers = this.eventHandlers.get(TimeEventType.tick);
        if (tickHandlers != null) {
            TickEvent tickEvent = new TickEvent();
            for (PipelineHandler tickHandler : tickHandlers) {
                try {
                    tickHandler.handleEvent(context, tickEvent);
                } catch (Exception e) {
                    System.out.printf("An error occurred processing event '%s' in handler '%s':\n", tickEvent, tickHandler.getClass().getSimpleName());
                    e.printStackTrace();
                }
            }
        }

    }

    private void monitorFrameRate() {
        if (nextEventFrameMillis == 0) {
            nextEventFrameMillis = System.currentTimeMillis() + 1000;
        } else if (System.currentTimeMillis() > nextEventFrameMillis) {
            context.postEvent(new FrameRateEvent(eventsInSecond));
            nextEventFrameMillis = System.currentTimeMillis() + 1000;
            eventsInSecond = 0;
        }
        eventsInSecond++;
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
