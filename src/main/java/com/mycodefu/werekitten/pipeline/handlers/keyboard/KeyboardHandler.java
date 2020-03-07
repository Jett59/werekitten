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
                                case LEFT: {
                                    if (!keysDown.contains(KeyCode.LEFT)){
                                        keysDown.add(KeyCode.LEFT);
                                        context.postEvent(new LeftKeyPressedEvent());
                                    }
                                    break;
                                }
                                case RIGHT: {
                                    if (!keysDown.contains(KeyCode.RIGHT)){
                                        keysDown.add(KeyCode.RIGHT);
                                        context.postEvent(new RightKeyPressedEvent());
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
                            }
                        };
                        context.getStage().addEventHandler(KeyEvent.KEY_PRESSED, keyPressedEventHandler);

                        keyReleasedEventHandler = keyEvent -> {
                                switch (keyEvent.getCode()) {
                                    case LEFT: {
                                        context.postEvent(new LeftKeyReleasedEvent());
                                        keysDown.remove(KeyCode.LEFT);
                                        break;
                                    }
                                    case RIGHT: {
                                        context.postEvent(new RightKeyReleasedEvent());
                                        keysDown.remove(KeyCode.RIGHT);
                                        break;
                                    }
                                    case SPACE: {
                                        context.postEvent(new SpaceKeyReleasedEvent());
                                        keysDown.remove(KeyCode.SPACE);
                                        break;
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