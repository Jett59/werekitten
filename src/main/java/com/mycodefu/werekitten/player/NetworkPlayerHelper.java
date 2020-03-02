package com.mycodefu.werekitten.player;

import com.mycodefu.werekitten.event.KeyboardEventType;
import com.mycodefu.werekitten.network.message.MessageBuilder;
import com.mycodefu.werekitten.network.message.MessageType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.events.keyboard.RegisterKeyListenerEvent;
import com.mycodefu.werekitten.pipeline.events.player.JumpEvent;
import com.mycodefu.werekitten.pipeline.events.player.MoveLeftEvent;
import com.mycodefu.werekitten.pipeline.events.player.MoveMode;
import com.mycodefu.werekitten.pipeline.events.player.MoveRightEvent;
import com.mycodefu.werekitten.pipeline.events.player.StopMovingLeftEvent;
import com.mycodefu.werekitten.pipeline.events.player.StopMovingRightEvent;
import com.mycodefu.werekitten.pipeline.events.ui.*;
import com.mycodefu.werekitten.ui.GameUI;
import io.netty.buffer.ByteBuf;
import javafx.util.Duration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkPlayerHelper implements RegisterKeyListenerEvent.KeyListener {
    private double scaledXMove = 2;

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
                    playerMessageSender.sendMessage(MessageBuilder.createNewMessageBuffer(MessageType.moveLeft, 1).getBuffer());
                    break;
                case rightPressed:
                    playerMessageSender.sendMessage(MessageBuilder.createNewMessageBuffer(MessageType.moveRight, 1).getBuffer());
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
        this.scaledXMove=context.level().get().getPixelScaleHelper().scaleX(2);

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
                    context.postEvent(new MoveLeftEvent(playerId, xScaled, MoveMode.MoveTo));
                } else {
                    context.postEvent(new MoveRightEvent(playerId, xScaled, MoveMode.MoveTo));
                }
                break;
            }
            case moveLeft: {
                context.postEvent(new MoveLeftEvent(playerId, this.scaledXMove, MoveMode.MoveBy));
                break;
            }
            case moveRight: {
                context.postEvent(new MoveRightEvent(playerId, this.scaledXMove, MoveMode.MoveBy));
                break;
            }
            case idleLeft: {
                context.postEvent(new StopMovingLeftEvent(playerId));
                break;
            }
            case idleRight: {
                context.postEvent(new StopMovingRightEvent(playerId));
                break;
            }
            case jump: {
                context.postEvent(new JumpEvent(playerId));
            }
        }
    }
}
