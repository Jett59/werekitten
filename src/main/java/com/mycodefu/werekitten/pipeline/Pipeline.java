package com.mycodefu.werekitten.pipeline;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Pipeline {
    private Queue<PipelineEvent> eventQueue = new ConcurrentLinkedQueue<>();
    private int eventsToRunPerFrame = 5;
    public void processEvents(){
for(int n = 0; n < eventsToRunPerFrame; n++) {
	
}
    }
    
    public void addEvent(PipelineEvent event) {
    	eventQueue.offer(event);
    }
    
    public Pipeline(int eventsToRunPerFrame) {
    	this.eventsToRunPerFrame = eventsToRunPerFrame;
    }
}
