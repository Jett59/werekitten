package com.mycodefu.werekitten.pipeline.events.player;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.PlayerEventType;

public class JumpEvent extends PlayerEvent {

    public JumpEvent(String playerId) {
		super(playerId);
	}

	@Override
    public Event getEvent() {
        return PlayerEventType.jump;
    }
}
