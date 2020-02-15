package com.mycodefu.werekitten.pipeline.handlers.Ui;

import com.mycodefu.werekitten.event.UiEventType;
import com.mycodefu.werekitten.level.PixelScaleHelper;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.ui.*;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.player.Kitten;
import com.mycodefu.werekitten.player.Player;
import com.mycodefu.werekitten.ui.UI;

public class UiNetworkEventHandler implements PipelineHandler {
	private UI ui;

	@Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        if (event.getPipelineName().equalsIgnoreCase("Ui")) {
			PixelScaleHelper pixelScaleHelper = context.level().get() != null ? context.level().get().getPixelScaleHelper() : null;
			switch ((UiEventType)event.getEvent()) {
				case UiCreated: {
					this.ui = ((UiCreatedEvent) event).getUI();
					break;
				}
				case networkMoveLeft:{
					Player player = context.getPlayerMap().get(((NetworkMoveLeftEvent)event).getPlayerId());
					player.moveLeft(pixelScaleHelper.scaleX(Kitten.MOVE_AMOUNT));
					break;
				}
				case networkMoveRight:{
					Player player = context.getPlayerMap().get(((NetworkMoveRightEvent)event).getPlayerId());
					player.moveRight(pixelScaleHelper.scaleX(Kitten.MOVE_AMOUNT));
					break;
				}
				case networkStopMovingLeft:{
					Player player = context.getPlayerMap().get(((NetworkStopMovingLeftEvent)event).getPlayerId());
					player.stopMovingLeft();
					break;
				}
				case networkStopMovingRight:{
					Player player = context.getPlayerMap().get(((NetworkStopMovingRightEvent)event).getPlayerId());
					player.stopMovingRight();
					break;
				}
				case networkJump:{
					Player player = context.getPlayerMap().get(((NetworkJumpEvent)event).getPlayerId());
					player.jump();
					break;
				}
				case networkServerListening: {
					ui.setAddress(((NetworkServerListeningEvent)event).getAddress());
					break;
				}
                default:
                    break;
            }
        }
    }

}
