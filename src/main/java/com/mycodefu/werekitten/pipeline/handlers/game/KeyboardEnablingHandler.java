package com.mycodefu.werekitten.pipeline.handlers.game;

import com.mycodefu.werekitten.keyboard.KeyboardListener;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.keyboard.*;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;

import javafx.scene.input.KeyEvent;

public class KeyboardEnablingHandler implements PipelineHandler{

	@Override
	public void handleEvent(PipelineContext context, PipelineEvent event) {
		if(event.getPipelineName().equalsIgnoreCase("game")) {
			switch (event.getEvent().getName()) {
			case "start": {
				KeyboardListener keyboardListener = new KeyboardListener(type-> {
					switch (type) {
					case left: {
						context.postEvent(new LeftKeyPressedEvent());
						break;
					}
					case right: {
						context.postEvent(new RightKeyPressedEvent());
						break;
					}
					case space: {
						context.postEvent(new SpaceKeyPressedEvent());
						break;
					}
					
					default:
						break;
					}
				});
				keyboardListener.addKeyboardReleasedCallback(type->{
					switch (type) {
					case left: {
						context.postEvent(new LeftKeyReleasedEvent());
						break;
					}
					case right: {
						context.postEvent(new RightKeyReleasedEvent());
						break;
					}
					case space: {
						context.postEvent(new SpaceKeyReleasedEvent());
						break;
					}
					
					default:
						break;
					}
				});
				context.getStage().addEventHandler(KeyEvent.KEY_PRESSED, keyEvent->{
keyboardListener.keyPressed(keyEvent.getCode());
				});
				context.getStage().addEventHandler(KeyEvent.KEY_RELEASED, keyEvent->{
					keyboardListener.keyReleased(keyEvent.getCode());
				});
				break;
			}
			
			default:
				break;
			}
		}else {
			throw new IllegalArgumentException("the gameHandler keyboardEnablingHandler is not allowed to be in the pipeline "+event.getPipelineName());
		}
	}

}
