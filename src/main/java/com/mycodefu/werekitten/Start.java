package com.mycodefu.werekitten;


import com.mycodefu.werekitten.pipeline.Pipeline;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.config.PipelineConfig;
import com.mycodefu.werekitten.pipeline.config.PipelineConfiguration;
import com.mycodefu.werekitten.pipeline.events.game.StartGameEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.player.Player;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Start extends Application implements PipelineContext {
    private final Map<String, Pipeline> pipelines;
    private Stage stage;

    public Start() {
        this.pipelines = new HashMap<>();
        PipelineConfig config = PipelineConfig.read();
        for (int i = 0; i < config.getPipelines().size(); i++) {
            PipelineConfiguration pipelineConfiguration = config.getPipelines().get(i);
            String name = pipelineConfiguration.getName();
            if (name == null || name.length() == 0) {
                throw new IllegalArgumentException("Configuration file has illegal empty pipeline name");
            }
            if (pipelines.containsKey(name)) {
                throw new IllegalArgumentException("Duplicate pipeline name '" + name + "'.");
            }
            List<PipelineHandler> handlers = new ArrayList<>();
            for (int j = 0; j < pipelineConfiguration.getHandlers().length; j++) {
                String handlerClass = pipelineConfiguration.getHandlers()[j];
                try {
                    handlers.add((PipelineHandler) Class.forName(handlerClass).newInstance());
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    e.printStackTrace();
                    throw new IllegalArgumentException("Failed to load handler '" + handlerClass + "'.", e);
                }
            }
            pipelines.put(name, new Pipeline(name, this, pipelineConfiguration.getEventsToRunPerFrame(), handlers.toArray(new PipelineHandler[]{})));
        }
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.postEvent(new StartGameEvent());

        new AnimationTimer() {
            @Override
            public void handle(long l) {
                for (Pipeline pipeline : pipelines.values()) {
                    pipeline.processEvents();
                }
            }
        }.start();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public Stage getStage() {
        return this.stage;
    }

    @Override
    public void postEvent(PipelineEvent event) {
        pipelines.get(event.getPipelineName()).addEvent(event);
    }

    private Map<String, Player> playerMap = new HashMap<>();
    
	@Override
	public Map<String, Player> getPlayerMap() {
		return playerMap;
	}
}
