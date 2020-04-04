package com.mycodefu.werekitten.pipeline.handlers.debug;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.KeyboardEventType;
import com.mycodefu.werekitten.event.PlayerEventType;
import com.mycodefu.werekitten.event.TimeEventType;
import com.mycodefu.werekitten.event.UiEventType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.keyboard.F3PressedEvent;
import com.mycodefu.werekitten.pipeline.events.keyboard.F3ReleasedEvent;
import com.mycodefu.werekitten.pipeline.events.player.PlayerEvent;
import com.mycodefu.werekitten.pipeline.events.time.FrameRateEvent;
import com.mycodefu.werekitten.pipeline.events.time.TickEvent;
import com.mycodefu.werekitten.pipeline.events.ui.PlayerCreatedEvent;
import com.mycodefu.werekitten.pipeline.events.ui.PlayerDestroyedEvent;
import com.mycodefu.werekitten.pipeline.events.ui.UiCreatedEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.ui.UI;

import javafx.scene.text.Text;

public class F3DebugHandler implements PipelineHandler {
    private UI ui;
    private Map<String, PlayerEvent> lastPlayerEvents;
    private long fps = 0;
    private Text debugText = new Text();

    public F3DebugHandler() {
        lastPlayerEvents = new HashMap<>();
    }

    @Override
    public Event[] getEventInterest() {
        return Event.combineEvents(PlayerEventType.values(), new Event[]{TimeEventType.framerate, TimeEventType.tick, UiEventType.UiCreated, KeyboardEventType.F3Released, UiEventType.playerCreated, UiEventType.playerDestroyed});
    }

    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        if (event instanceof UiCreatedEvent) {
            this.ui = ((UiCreatedEvent) event).getUI();
            debugText.setTranslateY(150);
            debugText.setFocusTraversable(true);
            debugText.setVisible(false);
            this.ui.addNode(debugText);
        } else if (event instanceof PlayerDestroyedEvent) {
            lastPlayerEvents.remove(((PlayerDestroyedEvent) event).getPlayer().getId());
        } else if (event instanceof PlayerCreatedEvent) {
            lastPlayerEvents.put(((PlayerCreatedEvent) event).getPlayer().getId(), null);
        } else if (event instanceof F3ReleasedEvent) {
            debugText.setVisible(!debugText.isVisible());
        } else if (event instanceof PlayerEvent) {
            PlayerEvent playerEvent = (PlayerEvent) event;
            lastPlayerEvents.put(playerEvent.getPlayerId(), playerEvent);
        } else if (event instanceof FrameRateEvent) {
            fps = ((FrameRateEvent) event).getTicksPerSecond();
        } else if (event instanceof TickEvent) {
            String debugText = String.format("fps: %s\n", "" + fps);
            Set<String> keySet = new HashSet<>(lastPlayerEvents.keySet());
            for (String playerId : keySet) {
                double translateX = context.getPlayerMap().get(playerId).getGroup().getTranslateX();
                double scaledBackX = context.level().get().getPixelScaleHelper().scaleXBack(translateX);
                debugText += playerId + ": " + (lastPlayerEvents.get(playerId) != null ? lastPlayerEvents.get(playerId) : "this player has not recieved any events") + "\ncurrent x position: " + scaledBackX + "\n";
            }
            this.debugText.setText(debugText);
        }
    }

}
