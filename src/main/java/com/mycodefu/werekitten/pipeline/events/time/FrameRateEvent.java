package com.mycodefu.werekitten.pipeline.events.time;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.TimeEventType;
import com.mycodefu.werekitten.pipeline.PipelineEvent;

public class FrameRateEvent implements PipelineEvent {
    private long ticksPerSecond;

    public FrameRateEvent(long ticksPerSecond) {
        this.ticksPerSecond = ticksPerSecond;
    }

    public long getTicksPerSecond() {
        return ticksPerSecond;
    }

    @Override
    public String getPipelineName() {
        return "pipeline";
    }

    @Override
    public Event getEvent() {
        return TimeEventType.framerate;
    }
    
    public String toString() {
    	return "frame rate: "+ticksPerSecond;
    }
}
