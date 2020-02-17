package com.mycodefu.werekitten.pipeline.handlers.Ui;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.mycodefu.werekitten.event.UiEventType;
import com.mycodefu.werekitten.level.PixelScaleHelper;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.ui.*;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.player.Player;
import com.mycodefu.werekitten.ui.UI;

public class UiNetworkEventHandler implements PipelineHandler {
	private UI ui;
	private List<PipelineEvent> unusedNetworkEvents = new CopyOnWriteArrayList<>();

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
					player.moveLeft(pixelScaleHelper.scaleX(((NetworkMoveLeftEvent)event).amount));
					break;
				}
				case networkMoveRight:{
					Player player = context.getPlayerMap().get(((NetworkMoveRightEvent)event).getPlayerId());
					player.moveRight(pixelScaleHelper.scaleX(((NetworkMoveRightEvent)event).amount));
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
				case networkJump: {
					NetworkJumpEvent jumpEvent = (NetworkJumpEvent)event;
					if(checkPlayerIsValidAndExecuteEvents(jumpEvent.getPlayerId(), context)) {
						Player player = context.getPlayerMap().get(jumpEvent.getPlayerId());
						player.jump();
					}else {
						unusedNetworkEvents.add(event);
					}
					break;
				}
				case networkServerListening: {
					ui.setAddress(((NetworkServerListeningEvent)event).getAddress());
					break;
				}
                default:
                    break;
            }
        }else {
        	throw new IllegalArgumentException("the UiNetworkEventHandler may only exist in the UI pipeline, not the pipeline: "+event.getPipelineName());
        }
    }

private boolean checkPlayerIsValidAndExecuteEvents(String id, PipelineContext context) {
	Player player = context.getPlayerMap().get(id);
	if(player == null) {
		return false;
	}else {
		for(PipelineEvent event: unusedNetworkEvents) {
			handleEvent(context, event);
		}
		return true;
	}
}
}
