package com.mycodefu.werekitten.pipeline.handlers.chat;

import java.awt.Toolkit;

import com.mycodefu.werekitten.event.ChatEventType;
import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.UiEventType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.chat.ChatMessageRecievedEvent;
import com.mycodefu.werekitten.pipeline.events.ui.UiCreatedEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;

import javafx.application.Platform;
import javafx.scene.AccessibleAction;
import javafx.scene.control.TextArea;

public class ChatMessageDisplayHandler implements PipelineHandler{
private TextArea displayArea;

public ChatMessageDisplayHandler() {
	this.displayArea = new TextArea();
	displayArea.setEditable(false);
}

	@Override
	public Event[] getEventInterest() {
		return new Event[] {
				ChatEventType.send,
				ChatEventType.recieved,
				UiEventType.UiCreated
		};
	}

	@Override
	public void handleEvent(PipelineContext context, PipelineEvent event) {
		if(event instanceof UiCreatedEvent) {
			displayArea.setTranslateX(0);
			displayArea.setTranslateY(Toolkit.getDefaultToolkit().getScreenSize().height-200);
			((UiCreatedEvent)event).getUI().addNode(displayArea);
		}else if(event instanceof ChatMessageRecievedEvent) {
			ChatMessageRecievedEvent chatEvent = (ChatMessageRecievedEvent)event;
			displayArea.setText(chatEvent.message+"\n"+displayArea.getText());
		}
	}

}
