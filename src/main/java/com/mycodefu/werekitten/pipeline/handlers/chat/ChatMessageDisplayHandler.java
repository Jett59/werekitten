package com.mycodefu.werekitten.pipeline.handlers.chat;

import com.mycodefu.werekitten.event.ChatEventType;
import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.KeyboardEventType;
import com.mycodefu.werekitten.event.UiEventType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.chat.ChatMessageRecievedEvent;
import com.mycodefu.werekitten.pipeline.events.chat.ChatMessageSendEvent;
import com.mycodefu.werekitten.pipeline.events.keyboard.CKeyPressedEvent;
import com.mycodefu.werekitten.pipeline.events.soundeffects.PlaySoundEffectEvent;
import com.mycodefu.werekitten.pipeline.events.soundeffects.SoundEffects;
import com.mycodefu.werekitten.pipeline.events.ui.UiCreatedEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import javafx.scene.AccessibleRole;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;

import java.awt.*;

public class ChatMessageDisplayHandler implements PipelineHandler {
    private TextArea displayArea;
    private HBox chatSendInterface;
    private TextField textField;
    private Button send;
    private Group group;

    public ChatMessageDisplayHandler() {
        this.displayArea = new TextArea();
        displayArea.setEditable(false);
        this.textField = new TextField();
        textField.setAccessibleRole(AccessibleRole.TEXT_AREA);
        this.send = new Button("send");
        this.chatSendInterface = new HBox(20, textField, send);
        group = new Group(displayArea, chatSendInterface);
    }

    @Override
    public Event[] getEventInterest() {
        return new Event[]{
                ChatEventType.send,
                ChatEventType.recieved,
                UiEventType.UiCreated,
                KeyboardEventType.cPressed
        };
    }

    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        if (event instanceof UiCreatedEvent) {
            chatSendInterface.setTranslateX(Toolkit.getDefaultToolkit().getScreenSize().width - 400);
            chatSendInterface.setTranslateY(50);
            textField.setPrefColumnCount(20);
            displayArea.setTranslateX(0);
            displayArea.setTranslateY(Toolkit.getDefaultToolkit().getScreenSize().height - 200);
            ((UiCreatedEvent) event).getUI().addNode(group);

            textField.setOnKeyPressed(keyEvent -> {
            	if (keyEvent.getCode()== KeyCode.ENTER){
            	    send.fire();
                }
			});

            send.setOnAction(e -> {
                String message = textField.getText();
                context.postEvent(new ChatMessageSendEvent(message));
                displayArea.setText(String.format("Sent: %s\n%s", message, displayArea.getText()));

                textField.setText("");
                textField.requestFocus();
            });

        } else if (event instanceof ChatMessageRecievedEvent) {
            ChatMessageRecievedEvent chatEvent = (ChatMessageRecievedEvent) event;
            displayArea.setText("Received: " + chatEvent.message + "\n" + displayArea.getText());

            context.postEvent(new PlaySoundEffectEvent(SoundEffects.bell));

        } else if (event instanceof CKeyPressedEvent) {
            System.out.println("group.isVisible: " + group.isVisible());
            group.setVisible(!group.isVisible());
            System.out.println("group.isVisible: " + group.isVisible());
        }
    }

}
