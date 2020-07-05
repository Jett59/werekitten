package com.mycodefu.werekitten.player;

import com.mycodefu.werekitten.Start;
import com.mycodefu.werekitten.network.message.MessageBuilder;
import com.mycodefu.werekitten.network.message.MessageType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.events.chat.ChatMessageRecievedEvent;
import com.mycodefu.werekitten.pipeline.events.player.*;
import com.mycodefu.werekitten.pipeline.events.ui.NetworkConnectedEvent;
import com.mycodefu.werekitten.pipeline.events.ui.NetworkDisconnectedEvent;
import com.mycodefu.werekitten.pipeline.events.ui.PlayerCreatedEvent;
import com.mycodefu.werekitten.pipeline.events.ui.PlayerDestroyedEvent;
import com.mycodefu.werekitten.ui.GameUI;
import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import javafx.util.Duration;

public class NetworkPlayerHelper {
    public interface NetworkPlayerMessageSender {
        void sendMessage(ByteBuf message);
    }

    public void createNetworkPlayer(String playerId, PipelineContext context, double initialXPosition) {
        double height = context.level().get().getPlayerElement().getSize().getHeight();

        double catJumpAmount = context.level().get().getPixelScaleHelper().scaleY(GameUI.CAT_JUMP_AMOUNT);

        Player networkPlayer = Kitten.create(playerId, catJumpAmount, height, Duration.seconds(1), Player.AnimationType.idleLeft, initialXPosition, context.level().get().getPixelScaleHelper());
        context.getPlayerMap().put(playerId, networkPlayer);
        context.postEvent(new PlayerCreatedEvent(networkPlayer));
        context.postEvent(new NetworkConnectedEvent());
    }

    public void destroyNetworkPlayer(String playerId, PipelineContext context) {
        Player player = context.getPlayerMap().get(playerId);
        context.postEvent(new PlayerDestroyedEvent(player));
        context.postEvent(new NetworkDisconnectedEvent());
    }

    public void applyNetworkMessageToPlayer(ByteBuf content, String playerId, PipelineContext context, NetworkPlayerMessageSender playerMessageSender, boolean shouldSendInit) {
        MessageType messageType = MessageType.forCode(content.readByte());
        long timeSent = content.readLong();
        long latency = System.currentTimeMillis() - timeSent;
        if (Start.DEBUG_PIPELINE_EVENTS){
            System.out.printf("Latency: %d\n", latency);
        }
        switch (messageType) {
            case init: {
                if (shouldSendInit) {
                    Player local = context.getPlayerMap().get("local");
                    double x = context.level().get().getPixelScaleHelper().scaleXBack(local.getGroup().getLayoutX());
                    playerMessageSender.sendMessage(MessageBuilder.createNewMessageBuffer(MessageType.init, 2).addDoubleAsShort(x).getBuffer());
                }
                double initialXPosition = context.level().get().getPixelScaleHelper().scaleX(((double) content.readShort()) / 10);
                createNetworkPlayer(playerId, context, initialXPosition);
                break;
            }
            case moveLeft: {
                double x = ((double) content.readShort()) / 10d;
                double xScaled = context.level().get().getPixelScaleHelper().scaleX(x);
                context.postEvent(new MoveLeftEvent(playerId, xScaled));
                break;
            }
            case moveRight: {
                double x = ((double) content.readShort()) / 10d;
                double xScaled = context.level().get().getPixelScaleHelper().scaleX(x);
                context.postEvent(new MoveRightEvent(playerId, xScaled));
                break;
            }
            case idleLeft: {
                double x = ((double) content.readShort()) / 10d;
                double xScaled = context.level().get().getPixelScaleHelper().scaleX(x);
                context.postEvent(new StopMovingLeftEvent(playerId, xScaled));
                break;
            }
            case idleRight: {
                double x = ((double) content.readShort()) / 10d;
                double xScaled = context.level().get().getPixelScaleHelper().scaleX(x);
                context.postEvent(new StopMovingRightEvent(playerId, xScaled));
                break;
            }
            case jump: {
                context.postEvent(new JumpEvent(playerId));
                break;
            }
            case chat: {
            	byte[] bytes = new byte[content.readByte()];
            	content.readBytes(bytes);
            	String chatMessage = new String(bytes, CharsetUtil.UTF_8);
            	context.postEvent(new ChatMessageRecievedEvent(chatMessage));
            }
		default:
			break;
        }
    }
}
