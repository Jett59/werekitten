package com.mycodefu.werekitten.pipeline.events.ui;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.UiEventType;
import com.mycodefu.werekitten.ui.UI;

public class UiCreatedEvent extends UiEvent {
    private UI ui;

    public UiCreatedEvent(UI ui) {
        this.ui = ui;
    }

    public UI getUI() {
        return ui;
    }

    @Override
    public Event getEvent() {
        return UiEventType.UiCreated;
    }
}
