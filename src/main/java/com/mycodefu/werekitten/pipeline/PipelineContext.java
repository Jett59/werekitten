package com.mycodefu.werekitten.pipeline;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.mycodefu.werekitten.level.GameLevel;
import com.mycodefu.werekitten.player.Player;

import javafx.stage.Stage;

public interface PipelineContext {
    Stage getStage();
    Map<String, Player> getPlayerMap();
    void postEvent(PipelineEvent event);
    AtomicReference<GameLevel> level();
    int getListeningPort();
    Map<String,String> getPreferences();
}
