package com.mycodefu.werekitten.pipeline.events.game;

import com.mycodefu.werekitten.event.GameEventType;

public class StartGameEvent extends GameEvent {
    public StartGameEvent() {
        super(GameEventType.start);
    }
}
