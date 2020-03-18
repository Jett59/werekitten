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
import com.mycodefu.werekitten.pipeline.events.keyboard.KeyboardEvent;
import com.mycodefu.werekitten.pipeline.events.player.PlayerEvent;
import com.mycodefu.werekitten.pipeline.events.time.FrameRateEvent;
import com.mycodefu.werekitten.pipeline.events.time.TickEvent;
import com.mycodefu.werekitten.pipeline.events.ui.UiCreatedEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.ui.UI;

import javafx.scene.text.Text;

public class F3DebugHandler implements PipelineHandler{
	private UI ui;
private Map<String, PlayerEvent> lastPlayerEvents;
private long fps = 0;
private Text debugText = new Text();

public F3DebugHandler() {
lastPlayerEvents = new HashMap<>();
}

	@Override
	public Event[] getEventInterest() {
		return Event.combineEvents(PlayerEventType.values(), new Event[] {TimeEventType.framerate, TimeEventType.tick, UiEventType.UiCreated, KeyboardEventType.F3Pressed, KeyboardEventType.F3Released});
	}

	@Override
	public void handleEvent(PipelineContext context, PipelineEvent event) {
		if(event instanceof UiCreatedEvent) {
			this.ui = ((UiCreatedEvent)event).getUI();
			debugText.setTranslateY(150);
			debugText.setFocusTraversable(true);
			debugText.setVisible(false);
			this.ui.addNode(debugText);
		}else if(event instanceof F3PressedEvent) {
		debugText.setVisible(!debugText.isVisible());
		}else if(event instanceof PlayerEvent) {
			PlayerEvent playerEvent = (PlayerEvent)event;
			lastPlayerEvents.put(playerEvent.getPlayerId(), playerEvent);
		}else if(event instanceof FrameRateEvent) {
			fps = ((FrameRateEvent)event).getTicksPerSecond();
		}else if(event instanceof TickEvent) {
			String debugText = String.format("fps: %s\n", ""+fps);
			Set<String> keySet = new HashSet<>(lastPlayerEvents.keySet());
			for(String playerId : keySet) {
				debugText+=playerId+": "+lastPlayerEvents.get(playerId)+"\n";
			}
			this.debugText.setText(debugText);
					}
	}

}
