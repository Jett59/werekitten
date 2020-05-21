package com.mycodefu.werekitten.pipeline.handlers.chat;

import java.awt.Toolkit;

import com.mycodefu.werekitten.event.ChatEventType;
import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.UiEventType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.chat.ChatMessageSendEvent;
import com.mycodefu.werekitten.pipeline.events.ui.UiCreatedEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class ChatMessageSendHandler implements PipelineHandler{
	private HBox chatSendInterface;
private TextField textField;
private Button send;

public ChatMessageSendHandler() {
	this.textField = new TextField();
	this.send = new Button("send");
	this.chatSendInterface = new HBox(20, textField, send);
}

	@Override
	public Event[] getEventInterest() {
		return new Event[] {ChatEventType.sent, UiEventType.UiCreated};
	}

	@Override
	public void handleEvent(PipelineContext context, PipelineEvent event) {
		if(event instanceof UiCreatedEvent) {
			chatSendInterface.setTranslateX(Toolkit.getDefaultToolkit().getScreenSize().width-400);
			chatSendInterface.setTranslateY(50);
			textField.setPrefColumnCount(20);
			((UiCreatedEvent)event).getUI().addNode(chatSendInterface);
			send.setOnAction(e->{
				context.postEvent(new ChatMessageSendEvent(textField.getText()));
			});
		}
	}

}
