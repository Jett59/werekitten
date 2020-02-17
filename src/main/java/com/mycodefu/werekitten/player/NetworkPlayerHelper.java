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
    public void keyEventOccurred(KeyboardEventType keyboardEventType, PipelineContext context) {
        for (NetworkPlayerMessageSender playerMessageSender : playerMessageSenders.values()) {
        String eventName = keyboardEventType.getName();
        if(eventName.contains("space")) {
        	playerMessageSender.sendMessage("jump");
        	System.out.println("sending jump message");
        }else if(eventName.contains("Pressed")) {
        double localX = context.getPlayerMap().get("local").getGroup().getTranslateX();
        playerMessageSender.sendMessage("move x to:"+(int)context.level().get().getPixelScaleHelper().scaleXBack(localX));
        System.out.println("sending move message");
        }else if(eventName.contains("released")) {
        playerMessageSender.sendMessage(eventName);
        }
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
    
    System.out.println("recieved message: "+message);
    if(message.contains("move to x:")) {
    	System.out.println("recieved move message: "+message);
    String newMessage = message.substring(10);
    double x = context.level().get().getPixelScaleHelper().scaleX((double)Integer.parseInt(newMessage));
    double oldX = context.getPlayerMap().get(playerId).getGroup().getTranslateX();
    double difference = x-oldX;
    //if(difference < 0) {
    	context.postEvent(new NetworkMoveLeftEvent(playerId, 0-difference));
    //}
    }
        switch (message) {
                        case "leftReleased": {
                context.postEvent(new NetworkStopMovingLeftEvent(playerId));
                break;
            }
            case "rightReleased": {
                context.postEvent(new NetworkStopMovingRightEvent(playerId));
                break;
            }
            case "jump": {
                context.postEvent(new NetworkJumpEvent(playerId));
            }
        }
    }
}
