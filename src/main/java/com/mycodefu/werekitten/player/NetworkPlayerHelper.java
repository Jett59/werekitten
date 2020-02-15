package com.mycodefu.werekitten.player;

import com.mycodefu.werekitten.event.KeyboardEventType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.events.keyboard.RegisterKeyListenerEvent;
import com.mycodefu.werekitten.pipeline.events.ui.*;
import com.mycodefu.werekitten.ui.GameUI;
import javafx.util.Duration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkPlayerHelper implements RegisterKeyListenerEvent.KeyListener {
    public interface NetworkPlayerMessageSender {
        void sendMessage(String message);
    }

    private Map<String, NetworkPlayerMessageSender> playerMessageSenders = new ConcurrentHashMap<>();
    private boolean registeredSelfAsKeyListener = false;

    @Override
    public void keyEventOccurred(KeyboardEventType keyboardEventType) {
        for (NetworkPlayerMessageSender playerMessageSender : playerMessageSenders.values()) {
            playerMessageSender.sendMessage(keyboardEventType.getName());
        }
    }

    public void createNetworkPlayer(String playerId, PipelineContext context, NetworkPlayerMessageSender playerMessageSender, double initialXPosition) {
    	double height = context.level().get().getPlayerElement().getSize().getHeight();

        double catJumpAmount = context.level().get().getPixelScaleHelper().scaleY(GameUI.CAT_JUMP_AMOUNT);

        Player networkPlayer = Kitten.create(catJumpAmount, height, Duration.seconds(1), Player.AnimationType.idleLeft, initialXPosition);
        context.getPlayerMap().put(playerId, networkPlayer);
        context.postEvent(new PlayerCreatedEvent(networkPlayer));
        context.postEvent(new NetworkConnectedEvent());

        playerMessageSenders.put(playerId, playerMessageSender);

        if (!registeredSelfAsKeyListener) {
            registeredSelfAsKeyListener=true;
            context.postEvent(new RegisterKeyListenerEvent(this));
        }
    }

    public void destroyNetworkPlayer(String playerId, PipelineContext context) {
        Player player = context.getPlayerMap().get(playerId);
        context.postEvent(new PlayerDestroyedEvent(player));
        context.postEvent(new NetworkDisconnectedEvent());

        playerMessageSenders.remove(playerId);
    }

    public void applyNetworkMessageToPlayer(String message, String playerId, PipelineContext context, NetworkPlayerMessageSender playerMessageSender, boolean shouldSendInit) {

    if(message.startsWith("init")) {
    	if(shouldSendInit) {
    	Player local = context.getPlayerMap().get("local");
        int x = (int)context.level().get().getPixelScaleHelper().scaleXBack(local.getGroup().getTranslateX()+local.getGroup().getLayoutX());
        playerMessageSender.sendMessage("init"+x);
    	}
    	double initialXPosition = context.level().get().getPixelScaleHelper().scaleX(Integer.parseInt(message.substring(4)));
    	    	createNetworkPlayer(playerId, context, playerMessageSender, initialXPosition);
    	return;
    }
    
        switch (message) {
            case "leftPressed": {
                context.postEvent(new NetworkMoveLeftEvent(playerId));
                break;
            }
            case "rightPressed": {
                context.postEvent(new NetworkMoveRightEvent(playerId));
                break;
            }
            case "leftReleased": {
                context.postEvent(new NetworkStopMovingLeftEvent(playerId));
                break;
            }
            case "rightReleased": {
                context.postEvent(new NetworkStopMovingRightEvent(playerId));
                break;
            }
            case "spacePressed": {
                context.postEvent(new NetworkJumpEvent(playerId));
            }
        }
    }
}
