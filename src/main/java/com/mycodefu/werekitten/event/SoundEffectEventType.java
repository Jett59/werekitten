package com.mycodefu.werekitten.event;

public enum SoundEffectEventType implements Event{
	play;

	@Override
	public String getName() {
		return name();
	}

}
