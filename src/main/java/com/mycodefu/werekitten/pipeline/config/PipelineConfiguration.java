package com.mycodefu.werekitten.pipeline.config;

public class PipelineConfiguration {
    String name;
    int eventsToRunPerFrame;
    String[] handlers;

    public String getName() {
        return name;
    }

    public int getEventsToRunPerFrame() {
        return eventsToRunPerFrame;
    }

    public String[] getHandlers() {
        return handlers;
    }
}
