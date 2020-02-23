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

    public static final String NETWORK_MESSAGE_MOVE_TO = "moveToX";
    public static final String NETWORK_MESSAGE_IDLE_LEFT = "idleLeft";
    public static final String NETWORK_MESSAGE_IDLE_RIGHT = "idleRight";
    public static final String NETWORK_MESSAGE_JUMP = "jump";

    public interface NetworkPlayerMessageSender {
        void sendMessage(String message);
    }

    private Map<String, NetworkPlayerMessageSender> playerMessageSenders = new ConcurrentHashMap<>();
    private boolean registeredSelfAsKeyListener = false;

    @Override
    public void keyEventOccurred(KeyboardEventType keyboardEventType, PipelineContext context) {
        for (NetworkPlayerMessageSender playerMessageSender : playerMessageSenders.values()) {
            switch (keyboardEventType) {
                case leftPressed:
                case rightPressed:
                    double localX = context.getPlayerMap().get("local").getGroup().getTranslateX();
                    double scaledX = context.level().get().getPixelScaleHelper().scaleXBack(localX);
                    playerMessageSender.sendMessage(NETWORK_MESSAGE_MOVE_TO + scaledX);
                    break;
                case leftReleased:
                    playerMessageSender.sendMessage(NETWORK_MESSAGE_IDLE_LEFT);
                    break;
                case rightReleased:
                    playerMessageSender.sendMessage(NETWORK_MESSAGE_IDLE_RIGHT);
                    break;
                case spacePressed:
                    playerMessageSender.sendMessage(NETWORK_MESSAGE_JUMP);
            }
        }
    }

    public void createNetworkPlayer(String playerId, PipelineContext context, NetworkPlayerMessageSender playerMessageSender, double initialXPosition) {
        double height = context.level().get().getPlayerElement().getSize().getHeight();

        double catJumpAmount = context.level().get().getPixelScaleHelper().scaleY(GameUI.CAT_JUMP_AMOUNT);

        Player networkPlayer = Kitten.create(playerId, catJumpAmount, height, Duration.seconds(1), Player.AnimationType.idleLeft, initialXPosition);
        context.getPlayerMap().put(playerId, networkPlayer);
        context.postEvent(new PlayerCreatedEvent(networkPlayer));
        context.postEvent(new NetworkConnectedEvent());

        playerMessageSenders.put(playerId, playerMessageSender);

        if (!registeredSelfAsKeyListener) {
            registeredSelfAsKeyListener = true;
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

        if (message.startsWith("init")) {
            if (shouldSendInit) {
                Player local = context.getPlayerMap().get("local");
                double x = context.level().get().getPixelScaleHelper().scaleXBack(local.getGroup().getTranslateX() + local.getGroup().getLayoutX());
                playerMessageSender.sendMessage("init" + x);
            }
            double initialXPosition = context.level().get().getPixelScaleHelper().scaleX(Double.parseDouble(message.substring(4)));
            createNetworkPlayer(playerId, context, playerMessageSender, initialXPosition);
            return;
        }

        if (message.startsWith(NETWORK_MESSAGE_MOVE_TO)) {
            String xAsString = message.substring(NETWORK_MESSAGE_MOVE_TO.length());
            double x = context.level().get().getPixelScaleHelper().scaleX(Double.parseDouble(xAsString));
            double oldX = context.getPlayerMap().get(playerId).getGroup().getTranslateX();
            double difference = x - oldX;
            if(difference == 0) {
            	return;
            }else if(difference < 0) {
            context.postEvent(new NetworkMoveLeftEvent(playerId, x));
            }else {
            	context.postEvent(new NetworkMoveRightEvent(playerId, x));
            }
        }
        switch (message) {
            case NETWORK_MESSAGE_IDLE_LEFT: {
                context.postEvent(new NetworkStopMovingLeftEvent(playerId));
                break;
            }
            case NETWORK_MESSAGE_IDLE_RIGHT: {
                context.postEvent(new NetworkStopMovingRightEvent(playerId));
                break;
            }
            case NETWORK_MESSAGE_JUMP: {
                context.postEvent(new NetworkJumpEvent(playerId));
            }
        }
    }
}
