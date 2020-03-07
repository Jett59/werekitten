package com.mycodefu.werekitten.pipeline.handlers.Ui;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.UiEventType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.player.JumpEvent;
import com.mycodefu.werekitten.pipeline.events.player.MoveLeftEvent;
import com.mycodefu.werekitten.pipeline.events.player.MoveMode;
import com.mycodefu.werekitten.pipeline.events.player.MoveRightEvent;
import com.mycodefu.werekitten.pipeline.events.player.StopMovingLeftEvent;
import com.mycodefu.werekitten.pipeline.events.player.StopMovingRightEvent;
import com.mycodefu.werekitten.pipeline.events.ui.*;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.player.Player;
import com.mycodefu.werekitten.ui.UI;

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
                    Player player = context.getPlayerMap().get(((MoveLeftEvent) event).getPlayerId());

                    MoveLeftEvent networkMoveLeftEvent = (MoveLeftEvent) event;
                    if (networkMoveLeftEvent.getMode()==MoveMode.MoveTo) {
                        player.moveLeftTo(networkMoveLeftEvent.x);
                    } else {
                        player.moveLeft(networkMoveLeftEvent.x);
                    }
                    break;
                }
                case networkMoveRight: {
                    Player player = context.getPlayerMap().get(((MoveRightEvent) event).getPlayerId());
                    MoveRightEvent networkMoveRightEvent = (MoveRightEvent) event;
                    if (networkMoveRightEvent.getMode()==MoveMode.MoveTo) {
                        player.moveRightTo(networkMoveRightEvent.x);
                    } else {
                        player.moveRight(networkMoveRightEvent.x);
                    }
                    break;
                }
                case networkStopMovingLeft: {
                    Player player = context.getPlayerMap().get(((StopMovingLeftEvent) event).getPlayerId());
                    player.stopMovingLeft();
                    break;
                }
                case networkStopMovingRight: {
                    Player player = context.getPlayerMap().get(((StopMovingRightEvent) event).getPlayerId());
                    player.stopMovingRight();
                    break;
                }
                case networkJump: {
                    JumpEvent jumpEvent = (JumpEvent) event;
                    Player player = context.getPlayerMap().get(jumpEvent.getPlayerId());
                    player.jump();
                    break;
                }
                case networkServerListening: {
                    ui.setAddress(((NetworkServerListeningEvent) event).getAddress());
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
