package com.mycodefu.werekitten.pipeline;

import com.mycodefu.werekitten.event.Event;

public interface PipelineEvent {
    String getPipelineName();
    Event getEvent();
}
