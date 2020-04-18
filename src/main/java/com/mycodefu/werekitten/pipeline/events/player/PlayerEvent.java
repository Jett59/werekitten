package com.mycodefu.werekitten.pipeline.events.player;

import com.mycodefu.werekitten.event.KeyboardEventType;
import static com.mycodefu.werekitten.event.KeyboardEventType.*;
import com.mycodefu.werekitten.event.PlayerEventType;
import com.mycodefu.werekitten.pipeline.PipelineEvent;

public abstract class PlayerEvent implements PipelineEvent {
    private String playerId;

    @Override
    public String getPipelineName() {
        return "pipeline";
    }

    PlayerEvent(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public PlayerEventType getPlayerEvent() {
        return (PlayerEventType) getEvent();
    }
    
    public String toString() {
    	return getClass().getSimpleName()+", player: "+getPlayerId();
    }
    
    public static final KeyboardEventType[] playerKeyboardEventTypes = new KeyboardEventType[] {
    		leftPressed,
    		leftReleased,
    		rightPressed,
    		rightReleased,
    		spacePressed
    };
}
