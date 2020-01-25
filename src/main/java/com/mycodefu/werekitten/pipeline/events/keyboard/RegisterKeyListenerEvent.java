package com.mycodefu.werekitten.pipeline.events.keyboard;

import com.mycodefu.werekitten.event.KeyboardEventType;

public class RegisterKeyListenerEvent extends KeyboardEvent{
	private KeyListener keyListener;

	public RegisterKeyListenerEvent(KeyListener keyListener) {
		super(KeyboardEventType.registerListener);
		this.keyListener = keyListener;
	}

	public KeyListener getKeyListener() {
		return keyListener;
	}

	public interface KeyListener{
		void keyEventOccurred(KeyboardEventType keyboardEventType);
	}
}
