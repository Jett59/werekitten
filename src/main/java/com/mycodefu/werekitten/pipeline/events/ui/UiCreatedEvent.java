package com.mycodefu.werekitten.pipeline.events.ui;

import com.mycodefu.werekitten.event.UiEventType;
import com.mycodefu.werekitten.ui.UI;

public class UiCreatedEvent extends UiEvent{
private UI ui;

	public UiCreatedEvent(UI ui) {
		super(UiEventType.UiCreated);
		
		this.ui = ui;
	}

public UI getUI() {
	return ui;
}

}
