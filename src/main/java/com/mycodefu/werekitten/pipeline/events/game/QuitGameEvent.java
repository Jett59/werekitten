package com.mycodefu.werekitten.pipeline.events.game;

import com.mycodefu.werekitten.event.GameEventType;

public class QuitGameEvent extends GameEvent {
    public QuitGameEvent() {
        super(GameEventType.quit);
    }
}
