package com.mycodefu.werekitten.pipeline.handlers.keyboard;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.KeyboardEventType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.keyboard.KeyboardEvent;
import com.mycodefu.werekitten.pipeline.events.player.JumpEvent;
import com.mycodefu.werekitten.pipeline.events.player.MoveLeftEvent;
import com.mycodefu.werekitten.pipeline.events.player.MoveMode;
import com.mycodefu.werekitten.pipeline.events.player.MoveRightEvent;
import com.mycodefu.werekitten.pipeline.events.player.PlayerEvent;
import com.mycodefu.werekitten.pipeline.events.player.StopMovingLeftEvent;
import com.mycodefu.werekitten.pipeline.events.player.StopMovingRightEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.player.Kitten;

public class PlayerKeyboardHandler implements PipelineHandler {

    @Override
    public Event[] getEventInterest() {
        return PlayerEvent.playerKeyboardEventTypes;
    }

    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        if (event instanceof KeyboardEvent) {
            switch ((KeyboardEventType) event.getEvent()) {
                case leftPressed: {
                    context.postEvent(new MoveLeftEvent("local", context.level().get().getPixelScaleHelper().scaleX(Kitten.MOVE_AMOUNT), MoveMode.MoveBy));
                    break;
                }
                case leftReleased: {
                    context.postEvent(new StopMovingLeftEvent("local"));
                    break;
                }
                case rightPressed: {
                    context.postEvent(new MoveRightEvent("local", context.level().get().getPixelScaleHelper().scaleX(Kitten.MOVE_AMOUNT), MoveMode.MoveBy));
                    break;
                }
                case rightReleased: {
                    context.postEvent(new StopMovingRightEvent("local"));
                    break;
                }
                case spaceReleased: {
                    context.postEvent(new JumpEvent("local"));
                    break;
                }

                default:
                    break;
            }
        }
    }

}
