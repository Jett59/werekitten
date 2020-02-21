package com.mycodefu.werekitten.pipeline.handlers.game;

import com.mycodefu.werekitten.event.GameEventType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.ui.PlayerCreatedEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.player.Kitten;
import com.mycodefu.werekitten.player.Player;
import com.mycodefu.werekitten.ui.GameUI;
import javafx.util.Duration;

public class LocalPlayerAdditionHandler implements PipelineHandler {

    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        if (event.getPipelineName().equalsIgnoreCase("game")) {
            GameEventType gameEventType = (GameEventType) event.getEvent();
            switch (gameEventType) {
				case levelLoaded: {
                    double catHeight = context.level().get().getPlayerElement().getSize().getHeight();

                    double initialXPosition = context.level().get().getPixelScaleHelper().scaleX(500);
                    double catJumpAmount = context.level().get().getPixelScaleHelper().scaleY(GameUI.CAT_JUMP_AMOUNT);

                    Player localPlayer = Kitten.create("local", catJumpAmount, catHeight, Duration.seconds(1), Player.AnimationType.idleRight, initialXPosition);
                    context.getPlayerMap().put("local", localPlayer);
                    context.postEvent(new PlayerCreatedEvent(localPlayer));
                    break;
                }

                default:
                    break;
            }
        } else {
            throw new IllegalArgumentException("the handler LocalPlayerAdditionHandler is not allowed to be outside the pipeline game, current pipeline: " + event.getPipelineName());
        }
    }

}