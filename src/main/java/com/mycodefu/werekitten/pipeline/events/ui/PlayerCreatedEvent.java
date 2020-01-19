package com.mycodefu.werekitten.pipeline.events.ui;

import com.mycodefu.werekitten.event.UiEventType;
import com.mycodefu.werekitten.player.Player;

public class PlayerCreatedEvent extends UiEvent{
private Player player;

	public PlayerCreatedEvent(Player player) {
		super(UiEventType.playerCreated);
		
		this.player = player;
	}

public Player getPlayer() {
	return player;
}

}
