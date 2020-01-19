package com.mycodefu.werekitten.pipeline;

import java.util.Map;

import com.mycodefu.werekitten.player.Player;

import javafx.stage.Stage;

public interface PipelineContext {
    Stage getStage();
    Map<String, Player> getPlayerMap();
    void postEvent(PipelineEvent event);
}
