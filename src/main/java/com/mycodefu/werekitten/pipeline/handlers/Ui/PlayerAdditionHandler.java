package com.mycodefu.werekitten.pipeline.handlers.Ui;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.UiEventType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.ui.PlayerCreatedEvent;
import com.mycodefu.werekitten.pipeline.events.ui.PlayerDestroyedEvent;
import com.mycodefu.werekitten.pipeline.events.ui.UiCreatedEvent;
import com.mycodefu.werekitten.pipeline.events.ui.UiEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.player.Player;
import com.mycodefu.werekitten.ui.UI;

public class PlayerAdditionHandler implements PipelineHandler {
    private UI ui;

    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        if (event instanceof UiEvent) {
			UiEvent uiEvent = (UiEvent) event;
			switch (uiEvent.getUIEvent()) {
                case UiCreated: {
                    System.out.println("UI created");
                    this.ui = ((UiCreatedEvent) event).getUI();
                    break;
                }
                case playerCreated: {
					Player player = ((PlayerCreatedEvent) event).getPlayer();
					System.out.println("Player created: " + player);
					ui.addPlayer(player);
                    break;
                }
                case playerDestroyed: {
                    ui.removePlayer(((PlayerDestroyedEvent) event).getPlayer());
                    break;
                }
			default:
				break;
            }
        }
    }

    @Override
    public Event[] getEventInterest() {
        return new Event[]{
                UiEventType.UiCreated,
                UiEventType.playerCreated,
                UiEventType.playerDestroyed
        };
    }

}
