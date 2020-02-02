package com.mycodefu.werekitten.pipeline.handlers.keyboard;

import com.mycodefu.werekitten.event.KeyboardEventType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.keyboard.RegisterKeyListenerEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.player.Kitten;
import com.mycodefu.werekitten.level.PixelScaleHelper;

import java.util.HashSet;
import java.util.Set;

public class PlayerKeyboardHandler implements PipelineHandler {
	private Set<RegisterKeyListenerEvent.KeyListener> listeners = new HashSet<>();

	@Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        if (event.getPipelineName().equalsIgnoreCase("keyboard")) {
            PixelScaleHelper pixelScaleHelper = context.level().get().getPixelScaleHelper();
            KeyboardEventType keyboardEventType = (KeyboardEventType) event.getEvent();
			switch (keyboardEventType) {
                case leftPressed: {
                    context.getPlayerMap().get("local").moveLeft(pixelScaleHelper.scaleX(Kitten.MOVE_AMOUNT));
                    break;
                }
                case rightPressed: {
                    context.getPlayerMap().get("local").moveRight(pixelScaleHelper.scaleX(Kitten.MOVE_AMOUNT));
                    break;
                }
                case leftReleased: {
                    context.getPlayerMap().get("local").stopMovingLeft();
                    break;
                }
                case rightReleased: {
                    context.getPlayerMap().get("local").stopMovingRight();
                    break;
                }
                case spacePressed: {
                    context.getPlayerMap().get("local").jump();
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
				listener.keyEventOccurred(keyboardEventType);
			}
        }
    }

}
