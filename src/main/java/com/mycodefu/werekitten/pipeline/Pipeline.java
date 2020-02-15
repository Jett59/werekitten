package com.mycodefu.werekitten.pipeline;

import com.mycodefu.werekitten.Start;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Pipeline {
    private final Queue<PipelineEvent> eventQueue;
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
            for (int i = 0; i < handlers.length; i++) {
                if (handlers[i] == null) {
                    throw new IllegalArgumentException("No handler in the handlers list may be null.");
                }
            }
        }
        if (eventsToRunPerFrame < 1 || eventsToRunPerFrame > 10) {
            throw new IllegalArgumentException("eventsToRunPerFrame must be between 1 and 10.");
        }
        this.handlers = handlers;
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

            for (int i = 0; i < this.handlers.length; i++) {
                PipelineHandler handler = this.handlers[i];
                handler.handleEvent(context, event);
            }
        }
    }

    public void addEvent(PipelineEvent event) {
        eventQueue.offer(event);
    }

    @Override
    public String toString() {
        return "Pipeline{" +
                ", name='" + name + '\'' +
                ", handlers.length=" + handlers.length +
                '}';
    }
}
