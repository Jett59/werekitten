package com.mycodefu.werekitten.pipeline.handlers.keyboard;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.TimeEventType;
import com.mycodefu.werekitten.keyboard.KeyType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.keyboard.*;
import com.mycodefu.werekitten.pipeline.events.ui.UiEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;
import java.util.Map;

import static com.mycodefu.werekitten.event.UiEventType.UiCreated;

public class KeyboardHandler implements PipelineHandler {
    private final Map<KeyCode, KeyType> codeToType = new HashMap<>();

    private EventHandler<KeyEvent> keyPressedEventHandler;
    private EventHandler<KeyEvent> keyReleasedEventHandler;

    public KeyboardHandler() {
        codeToType.put(KeyCode.LEFT, KeyType.left);
        codeToType.put(KeyCode.RIGHT, KeyType.right);
        codeToType.put(KeyCode.SPACE, KeyType.space);
    }

    @Override
    public Event[] getEventInterest() {
        return new Event[]{UiCreated, TimeEventType.tick};
    }

    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        if (event instanceof UiEvent) {
            UiEvent uiEvent = (UiEvent) event;
            switch (uiEvent.getUIEvent()) {
                case UiCreated: {
                    if (keyPressedEventHandler == null || keyReleasedEventHandler == null) {
                        keyPressedEventHandler = keyEvent -> {
                            KeyType key = codeToType.get(keyEvent.getCode());
                            if (key != null) {
								switch (key) {
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
								}
                            }
                        };
                        context.getStage().addEventHandler(KeyEvent.KEY_PRESSED, keyPressedEventHandler);

                        keyReleasedEventHandler = keyEvent -> {
                            KeyType key = codeToType.get(keyEvent.getCode());
                            if (key != null) {
                                switch (key) {
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
                                }
                            }
                        };
                        context.getStage().addEventHandler(KeyEvent.KEY_RELEASED, keyReleasedEventHandler);
                        break;
                    }
                }
            }
        }
    }
}