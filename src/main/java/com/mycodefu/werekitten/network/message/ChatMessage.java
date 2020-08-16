package com.mycodefu.werekitten.network.message;

public class ChatMessage extends Message {
    public String text;

    public ChatMessage(long timeStamp, String text) {
        super(MessageType.chat, timeStamp);
        this.text = text;
    }

    public ChatMessage(String text) {
        super(MessageType.chat);
        this.text = text;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "text='" + text + '\'' +
                ", type=" + type +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
