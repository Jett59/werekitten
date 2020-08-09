package com.mycodefu.werekitten.network.message;

public class Message {
    public MessageType type;
    public long timeStamp;

    public Message(MessageType type, long timeStamp) {
        this.timeStamp = timeStamp;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
