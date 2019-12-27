package com.mycodefu.werekitten.keyboard;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.mycodefu.werekitten.event.PlayerEventType;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;

public class KeyboardListener {
private AnimationTimer timer;
private Map<KeyCode, KeyType> codeToType = new HashMap<>();
private KeyboardEventHandler eventHandler;

public Map<KeyType, AtomicBoolean> typeToDown = new ConcurrentHashMap<>();

public static interface KeyboardEventHandler{
	void raiseEvent(PlayerEventType type);
}

public KeyboardListener(KeyboardEventHandler eventHandler) {
	this.eventHandler = eventHandler;
	
	codeToType.put(KeyCode.LEFT, KeyType.left);
	codeToType.put(KeyCode.RIGHT, KeyType.right);
	codeToType.put(KeyCode.SPACE, KeyType.space);

	typeToDown.put(KeyType.left, new AtomicBoolean());
	typeToDown.put(KeyType.right, new AtomicBoolean());
	typeToDown.put(KeyType.space, new AtomicBoolean());
}

public void startListening() {
	timer = new AnimationTimer() {

		@Override
		public void handle(long now) {
			//check if any of the keys in typeToDown are down
			if(typeToDown.get(KeyType.left).get()) {
				//raise an event using the eventHandler passed in the constructor
				eventHandler.raiseEvent(PlayerEventType.moveLeft);
			}else if(typeToDown.get(KeyType.right).get()) {
				//raise an event using the eventHandler passed in the constructor
				eventHandler.raiseEvent(PlayerEventType.moveRight);
			}else if(typeToDown.get(KeyType.space).get()) {
				//raise an event using the eventHandler passed in the constructor
				eventHandler.raiseEvent(PlayerEventType.jump);
			}
		}
		
	};
	timer.start();
}

//registers that the key is down
public void keyPressed(KeyCode code) {
	//the new value to be set on the map TypeToDown
	AtomicBoolean newValue = new AtomicBoolean();
	newValue.set(true);
	//get the KeyType equivilent of the KeyCode
	KeyType key = codeToType.get(code);
	//replace the value of the KeyType with the newValue
	typeToDown.replace(key, newValue);
}

//registers that the key is up
public void keyReleased(KeyCode code) {
	//the new value to be set on the map TypeToDown
	AtomicBoolean newValue = new AtomicBoolean();
	newValue.set(false);
	//get the KeyType equivilent of the KeyCode
	KeyType key = codeToType.get(code);
	//replace the value of the KeyType with the newValue
	typeToDown.replace(key, newValue);
}

}
