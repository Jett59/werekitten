package com.mycodefu.werekitten.pipeline.handlers.game;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.GameEventType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.game.GameEvent;
import com.mycodefu.werekitten.pipeline.events.ui.PlayerCreatedEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.player.Kitten;
import com.mycodefu.werekitten.player.Player;
import com.mycodefu.werekitten.ui.GameUI;
import javafx.util.Duration;

public class LocalPlayerAdditionHandler implements PipelineHandler {

    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        if (event instanceof GameEvent) {
            GameEvent gameEvent = (GameEvent)event;
            switch (gameEvent.getGameEvent()) {
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
        }
    }

	@Override
	public Event[] getEventInterest() {
		return new Event[] {
				GameEventType.levelLoaded
		};
	}

}