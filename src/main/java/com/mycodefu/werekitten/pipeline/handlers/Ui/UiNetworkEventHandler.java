package com.mycodefu.werekitten.pipeline.handlers.Ui;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.UiEventType;
import com.mycodefu.werekitten.level.PixelScaleHelper;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.ui.*;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.player.Player;
import com.mycodefu.werekitten.ui.UI;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UiNetworkEventHandler implements PipelineHandler {
    private UI ui;

    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        if (event instanceof UiEvent) {
            UiEvent uiEvent = (UiEvent) event;
            switch (uiEvent.getUIEvent()) {
                case UiCreated: {
                    this.ui = ((UiCreatedEvent) event).getUI();
                    break;
                }
                case networkMoveLeft: {
                    Player player = context.getPlayerMap().get(((NetworkMoveLeftEvent) event).getPlayerId());

                    NetworkMoveLeftEvent networkMoveLeftEvent = (NetworkMoveLeftEvent) event;
                    if (networkMoveLeftEvent.getMode()==NetworkMoveMode.MoveTo) {
                        player.moveLeftTo(networkMoveLeftEvent.x);
                    } else {
                        player.moveLeft(networkMoveLeftEvent.x);
                    }
                    break;
                }
                case networkMoveRight: {
                    Player player = context.getPlayerMap().get(((NetworkMoveRightEvent) event).getPlayerId());
                    NetworkMoveRightEvent networkMoveRightEvent = (NetworkMoveRightEvent) event;
                    if (networkMoveRightEvent.getMode()==NetworkMoveMode.MoveTo) {
                        player.moveRightTo(networkMoveRightEvent.x);
                    } else {
                        player.moveRight(networkMoveRightEvent.x);
                    }
                    break;
                }
                case networkStopMovingLeft: {
                    Player player = context.getPlayerMap().get(((NetworkStopMovingLeftEvent) event).getPlayerId());
                    player.stopMovingLeft();
                    break;
                }
                case networkStopMovingRight: {
                    Player player = context.getPlayerMap().get(((NetworkStopMovingRightEvent) event).getPlayerId());
                    player.stopMovingRight();
                    break;
                }
                case networkJump: {
                    NetworkJumpEvent jumpEvent = (NetworkJumpEvent) event;
                    Player player = context.getPlayerMap().get(jumpEvent.getPlayerId());
                    player.jump();
                    break;
                }
                case networkServerListening: {
                    ui.setAddress(((NetworkServerListeningEvent) event).getAddress());
                    break;
                }
            }
        }
    }

	@Override
	public Event[] getEventInterest() {
		return new Event[] {
				UiEventType.UiCreated,
				UiEventType.networkJump,
				UiEventType.networkMoveLeft,
				UiEventType.networkMoveRight,
				UiEventType.networkServerListening,
				UiEventType.networkStopMovingLeft,
				UiEventType.networkStopMovingRight
		};
	}
}
