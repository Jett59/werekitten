package com.mycodefu.werekitten;


import com.mycodefu.werekitten.level.GameLevel;
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

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Start extends Application implements PipelineContext {
    public static boolean DEBUG_PIPELINE_EVENTS = false;
    private static int listeningPort = 0;

    private final Map<String, Pipeline> pipelines;
    private Stage stage;
    private AtomicReference<GameLevel> level = new AtomicReference<>(null);

    @SuppressWarnings("deprecation")
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
            Pipeline pipeline = new Pipeline(name, this, pipelineConfiguration.getEventsToRunPerFrame(), handlers.toArray(new PipelineHandler[]{}));
            pipelines.put(name, pipeline);

            System.out.println(String.format("Initialized %s", pipeline));
        }
    }

    @Override
    public void start(@SuppressWarnings("exports") Stage stage) {
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
        if (args.length == 1) {
            try {
                listeningPort = Integer.parseInt(args[0]);
            } catch (Exception e) {
                System.out.println("Failed to listen on port " + args[0]);
            }
        }
        launch(args);
    }

    @SuppressWarnings("exports")
    @Override
    public Stage getStage() {
        return this.stage;
    }

    @SuppressWarnings("exports")
    @Override
    public void postEvent(PipelineEvent event) {
        Pipeline pipeline;
        try {
            pipeline = pipelines.get(event.getPipelineName());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("the pipeline " + event.getPipelineName() + "was not valid", e);
        }
        if (pipeline == null) {
            System.out.println("pipeline == null. event cannot be posted");
        } else {
            if (DEBUG_PIPELINE_EVENTS) {
                StackTraceElement calledBy = new Exception().getStackTrace()[1];
                System.out.println(String.format("%s (%s): Event posted: %s (%s), class: %s, \n%s\npostedBy: %s, method %s, line number: %s, module: %s", Instant.now().toString(), Thread.currentThread().getName(), event.getEvent(), event.getPipelineName(), event.getClass().getSimpleName(), event.toString(), calledBy.getClassName(), calledBy.getMethodName(), Integer.toString(calledBy.getLineNumber()), calledBy.getModuleName()));
            }
            pipeline.addEvent(event);
        }
    }

    @SuppressWarnings("exports")
    @Override
    public AtomicReference<GameLevel> level() {
        return this.level;
    }


    private Map<String, Player> playerMap = new HashMap<>();

    @SuppressWarnings("exports")
    @Override
    public Map<String, Player> getPlayerMap() {
        return playerMap;
    }

    @Override
    public int getListeningPort() {
        return listeningPort;
    }
}
	