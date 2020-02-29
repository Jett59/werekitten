package com.mycodefu.werekitten.player;

import com.mycodefu.werekitten.event.KeyboardEventType;
import com.mycodefu.werekitten.network.message.MessageBuilder;
import com.mycodefu.werekitten.network.message.MessageType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.events.keyboard.RegisterKeyListenerEvent;
import com.mycodefu.werekitten.pipeline.events.ui.*;
import com.mycodefu.werekitten.ui.GameUI;
import io.netty.buffer.ByteBuf;
import javafx.util.Duration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkPlayerHelper implements RegisterKeyListenerEvent.KeyListener {
    public interface NetworkPlayerMessageSender {
        void sendMessage(ByteBuf message);
    }

    private Map<String, NetworkPlayerMessageSender> playerMessageSenders = new ConcurrentHashMap<>();
    private boolean registeredSelfAsKeyListener = false;

    @Override
    public void keyEventOccurred(KeyboardEventType keyboardEventType, PipelineContext context) {
        for (NetworkPlayerMessageSender playerMessageSender : playerMessageSenders.values()) {
            switch (keyboardEventType) {
                case leftPressed:
                case rightPressed:
                    double localX = context.getPlayerMap().get("local").getGroup().getLayoutX();
                    double scaledX = context.level().get().getPixelScaleHelper().scaleXBack(localX);
                    playerMessageSender.sendMessage(MessageBuilder.createNewMessageBuffer(MessageType.move, 3).addDoubleAsShort(scaledX).getBuffer());
                    break;
                case leftReleased:
                    playerMessageSender.sendMessage(MessageBuilder.createNewMessageBuffer(MessageType.idleLeft, 1).getBuffer());
                    break;
                case rightReleased:
                    playerMessageSender.sendMessage(MessageBuilder.createNewMessageBuffer(MessageType.idleRight, 1).getBuffer());
                    break;
                case spacePressed:
                    playerMessageSender.sendMessage(MessageBuilder.createNewMessageBuffer(MessageType.jump, 1).getBuffer());
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

    public void applyNetworkMessageToPlayer(ByteBuf content, String playerId, PipelineContext context, NetworkPlayerMessageSender playerMessageSender, boolean shouldSendInit) {
        MessageType messageType = MessageType.forCode(content.readByte());
        switch (messageType) {
            case init: {
                if (shouldSendInit) {
                    Player local = context.getPlayerMap().get("local");
                    double x = context.level().get().getPixelScaleHelper().scaleXBack(local.getGroup().getLayoutX());
                    playerMessageSender.sendMessage(MessageBuilder.createNewMessageBuffer(MessageType.init, 3).addDoubleAsShort(x).getBuffer());
                }
                double initialXPosition = context.level().get().getPixelScaleHelper().scaleX(((double) content.readShort()) / 10);
                createNetworkPlayer(playerId, context, playerMessageSender, initialXPosition);
                break;
            }
            case move: {
                double x = ((double) content.readShort()) / 10;
                double xScaled = context.level().get().getPixelScaleHelper().scaleX(x);
                double oldX = context.getPlayerMap().get(playerId).getGroup().getLayoutX();
                double difference = xScaled - oldX;
                if (difference == 0) {
                    break;
                } else if (difference < 0) {
                    context.postEvent(new NetworkMoveLeftEvent(playerId, xScaled));
                } else {
                    context.postEvent(new NetworkMoveRightEvent(playerId, xScaled));
                }
                break;
            }
            case idleLeft: {
                context.postEvent(new NetworkStopMovingLeftEvent(playerId));
                break;
            }
            case idleRight: {
                context.postEvent(new NetworkStopMovingRightEvent(playerId));
                break;
            }
            case jump: {
                context.postEvent(new NetworkJumpEvent(playerId));
            }
        }
    }
}
