package com.mycodefu.werekitten.network.message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum MessageType {
    init((byte) 0),
    move((byte) 1),
    jump((byte) 2),
    idleLeft((byte) 3),
    idleRight((byte) 4),
	moveLeft((byte) 5),
	moveRight((byte) 6);

    private final byte code;

    MessageType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    private static Map<Byte, MessageType> codeToEnum;
    private static Object lockObject = new Object();
    private static boolean hasCreatedMap = false;

    public static MessageType forCode(Byte code) {
        if (!hasCreatedMap) {
            synchronized (lockObject) {
                if (!hasCreatedMap) {
                    codeToEnum = new ConcurrentHashMap<>();
                    for (MessageType type : MessageType.values()) {
                        codeToEnum.put(type.getCode(), type);
                    }
                    hasCreatedMap = true;
                }
            }
        }
        return codeToEnum.get(code);
    }
}
