package com.mycodefu.werekitten.pipeline;

import com.mycodefu.werekitten.event.NetworkEventType;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Pipeline {
    private Queue<PipelineEvent> eventQueue = new ConcurrentLinkedQueue<>();
    public void processEvents(){

    }
}
