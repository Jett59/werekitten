package com.mycodefu.werekitten.pipeline.handlers.keyboard;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.KeyboardEventType;
import static com.mycodefu.werekitten.pipeline.events.keyboard.RegisterKeyListenerEvent.KeyListener;

import java.util.ArrayList;
import java.util.List;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.keyboard.KeyboardEvent;
import com.mycodefu.werekitten.pipeline.events.keyboard.RegisterKeyListenerEvent;
import com.mycodefu.werekitten.pipeline.events.keyboard.UnregisterKeyListenerEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;

public class KeyboardHandler implements PipelineHandler {
private List<KeyListener> registeredKeyListeners = new ArrayList<>();

	@Override
	public Event[] getEventInterest() {
		return KeyboardEventType.values();
	}

	@Override
	public void handleEvent(PipelineContext context, PipelineEvent event) {
		if(event instanceof KeyboardEvent) {
			switch ((KeyboardEventType)event.getEvent()) {
			case registerListener: {
				registeredKeyListeners.add(((RegisterKeyListenerEvent)event).getKeyListener());
				break;
			}
			case unregisterListener: {
				registeredKeyListeners.remove(((UnregisterKeyListenerEvent)event).getKeyListener());
				break;
			}
			
			default: {
				for(KeyListener keyListener : registeredKeyListeners) {
					keyListener.keyEventOccurred((KeyboardEventType)event.getEvent(), context);
				}
			}
			}
		}
	}

}
