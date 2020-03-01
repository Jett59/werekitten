package com.mycodefu.werekitten.pipeline.handlers.keyboard;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.KeyboardEventType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.keyboard.KeyboardEvent;
import com.mycodefu.werekitten.pipeline.events.keyboard.RegisterKeyListenerEvent;
import com.mycodefu.werekitten.pipeline.events.network.NetworkEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.player.Kitten;
import com.mycodefu.werekitten.level.PixelScaleHelper;
import com.mycodefu.werekitten.player.Player;

import java.util.HashSet;
import java.util.Set;

public class PlayerKeyboardHandler implements PipelineHandler {
	private Set<RegisterKeyListenerEvent.KeyListener> listeners = new HashSet<>();

	@Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
		if(context.level().get() == null) {
			return;
		}
        if (event.getPipelineName().equalsIgnoreCase("pipeline") && event instanceof KeyboardEvent) {
            PixelScaleHelper pixelScaleHelper = context.level().get().getPixelScaleHelper();
            KeyboardEventType keyboardEventType = (KeyboardEventType) event.getEvent();
            Player localPlayer = context.getPlayerMap().get("local");

            switch (keyboardEventType) {
                case leftPressed: {
                    localPlayer.moveLeft(pixelScaleHelper.scaleX(Kitten.MOVE_AMOUNT));
                    break;
                }
                case rightPressed: {
                    localPlayer.moveRight(pixelScaleHelper.scaleX(Kitten.MOVE_AMOUNT));
                    break;
                }
                case leftReleased: {
                    localPlayer.stopMovingLeft();
                    break;
                }
                case rightReleased: {
                    localPlayer.stopMovingRight();
                    break;
                }
                case spacePressed: {
                    localPlayer.jump();
                    break;
                }
                case registerListener:{
                	this.listeners.add(((RegisterKeyListenerEvent)event).getKeyListener());
					return;
				}
                case unregisterListener: {
					this.listeners.remove(((RegisterKeyListenerEvent) event).getKeyListener());
					return;
				}
            }
			for (RegisterKeyListenerEvent.KeyListener listener : listeners) {
				listener.keyEventOccurred(keyboardEventType, context);
			}
        }
    }

	@Override
	public Event[] getEventInterest() {
		// TODO Auto-generated method stub
		return KeyboardEventType.values();
	}

}
