package com.mycodefu.werekitten.pipeline.handlers.preferences;

import java.util.HashMap;
import java.util.Map;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.GameEventType;
import com.mycodefu.werekitten.level.LevelReader;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.game.StartGameEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.preferences.Preferences;

public class PreferenceInitializationHandler implements PipelineHandler{
public static Map<String, String> defaultValues = new HashMap<>();
static {
	defaultValues.put(Preferences.CLIENT_CONNECT_IP_PREFERENCE, "");
	defaultValues.put(Preferences.DEFAULT_LEVEL_PREFERENCE, LevelReader.defaultLevelName);
	defaultValues.put(Preferences.LISTENING_PORT_PREFERENCE, "0");
}

	@Override
	public Event[] getEventInterest() {
		return new Event[] {GameEventType.start};
	}

	@Override
	public void handleEvent(PipelineContext context, PipelineEvent event) {
		if(event instanceof StartGameEvent) {
			defaultValues.forEach((key, value)->{
				if(!context.getPreferences().containsKey(key)) {
					context.getPreferences().put(key, value);
				}
			});
		}
	}

}
