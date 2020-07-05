package com.mycodefu.werekitten.pipeline.handlers.keyboard;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.KeyboardEventType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.keyboard.KeyboardEvent;
import com.mycodefu.werekitten.pipeline.events.player.JumpEvent;
import com.mycodefu.werekitten.pipeline.events.player.MoveLeftEvent;
import com.mycodefu.werekitten.pipeline.events.player.MoveRightEvent;
import com.mycodefu.werekitten.pipeline.events.player.PlayerEvent;
import com.mycodefu.werekitten.pipeline.events.player.StopMovingLeftEvent;
import com.mycodefu.werekitten.pipeline.events.player.StopMovingRightEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;

public class PlayerKeyboardHandler implements PipelineHandler {

    @Override
    public Event[] getEventInterest() {
        return PlayerEvent.playerKeyboardEventTypes;
    }

    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        if (event instanceof KeyboardEvent) {
            switch ((KeyboardEventType) event.getEvent()) {
                case aPressed: {
                    context.postEvent(new MoveLeftEvent("local"));
                    break;
                }
                case aReleased: {
                    context.postEvent(new StopMovingLeftEvent("local"));
                    break;
                }
                case dPressed: {
                    context.postEvent(new MoveRightEvent("local"));
                    break;
                }
                case dReleased: {
                    context.postEvent(new StopMovingRightEvent("local"));
                    break;
                }
                case spacePressed: {
                    context.postEvent(new JumpEvent("local"));
                    break;
                }

                default:
                    break;
            }
        }
    }

}
