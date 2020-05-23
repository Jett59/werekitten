package com.mycodefu.werekitten.pipeline.handlers.keyboard;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.TimeEventType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.keyboard.*;
import com.mycodefu.werekitten.pipeline.events.ui.UiEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashSet;
import java.util.Set;

import static com.mycodefu.werekitten.event.UiEventType.UiCreated;

public class KeyboardHandler implements PipelineHandler {
    private Set<KeyCode> keysDown = new HashSet<>();

    private EventHandler<KeyEvent> keyPressedEventHandler;
    private EventHandler<KeyEvent> keyReleasedEventHandler;

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
                            switch (keyEvent.getCode()) {
                                case A: {
                                    if (!keysDown.contains(KeyCode.A)){
                                        keysDown.add(KeyCode.A);
                                        context.postEvent(new AKeyPressedEvent());
                                    }
                                    break;
                                }
                                case D: {
                                    if (!keysDown.contains(KeyCode.D)){
                                        keysDown.add(KeyCode.D);
                                        context.postEvent(new DKeyPressedEvent());
                                    }
                                    break;
                                }
                                case SPACE: {
                                    if (!keysDown.contains(KeyCode.SPACE)) {
                                        keysDown.add(KeyCode.SPACE);
                                        context.postEvent(new SpaceKeyPressedEvent());
                                    }
                                    break;
                                }
                                case F3: {
                                	if (!keysDown.contains(KeyCode.F3)) {
                                	keysDown.add(KeyCode.F3);
                                	context.postEvent(new F3PressedEvent());
                                	}
                                	break;
                                }
							default:
								break;
                            }
                        };
                        context.getStage().addEventHandler(KeyEvent.KEY_PRESSED, keyPressedEventHandler);

                        keyReleasedEventHandler = keyEvent -> {
                                switch (keyEvent.getCode()) {
                                    case A: {
                                        context.postEvent(new AKeyReleasedEvent());
                                        keysDown.remove(KeyCode.A);
                                        break;
                                    }
                                    case D: {
                                        context.postEvent(new DKeyReleasedEvent());
                                        keysDown.remove(KeyCode.D);
                                        break;
                                    }
                                    case SPACE: {
                                        context.postEvent(new SpaceKeyReleasedEvent());
                                        keysDown.remove(KeyCode.SPACE);
                                        break;
                                    }
                                    case F3: {
                                    	context.postEvent(new F3ReleasedEvent());
                                    	keysDown.remove(KeyCode.F3);
                                    	break;
                                    }
								default:
									break;
                            }
                        };
                        context.getStage().addEventHandler(KeyEvent.KEY_RELEASED, keyReleasedEventHandler);
                        break;
                    }
                }
			default:
				break;
            }
        }
    }
}