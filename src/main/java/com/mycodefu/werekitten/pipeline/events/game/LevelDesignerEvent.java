package com.mycodefu.werekitten.pipeline.events.game;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.GameEventType;

public class LevelDesignerEvent extends GameEvent {
    @Override
    public Event getEvent() {
        return GameEventType.levelDesigner;
    }
}
