package com.mycodefu.werekitten.pipeline;

import javafx.stage.Stage;

public interface PipelineContext {
    Stage getStage();
    void postEvent(PipelineEvent event);
}
