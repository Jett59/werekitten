package com.mycodefu.werekitten.network.message;

public class JoinMessage extends Message {
    public String id;

    public JoinMessage(long timeStamp, String id) {
        super(MessageType.join, timeStamp);
        this.id = id;
    }

    public JoinMessage(String id) {
        super(MessageType.join);
        this.id = id;
    }

    @Override
    public String toString() {
        return "JoinMessage{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
