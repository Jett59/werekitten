package com.mycodefu.werekitten.pipeline.events.ui;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.UiEventType;
import com.mycodefu.werekitten.player.Player;

public class PlayerDestroyedEvent extends UiEvent {
    private Player player;

    public PlayerDestroyedEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public Event getEvent() {
        return UiEventType.playerDestroyed;
    }
}
