package com.mycodefu.werekitten.pipeline.events.keyboard;

import com.mycodefu.werekitten.event.KeyboardEventType;

public class UnregisterKeyListenerEvent extends KeyboardEvent{
	private RegisterKeyListenerEvent.KeyListener keyListener;

	public UnregisterKeyListenerEvent(RegisterKeyListenerEvent.KeyListener keyListener) {
		super(KeyboardEventType.unregisterListener);
		this.keyListener = keyListener;
	}

	public RegisterKeyListenerEvent.KeyListener getKeyListener() {
		return keyListener;
	}
}
