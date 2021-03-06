package com.mycodefu.werekitten.event;

public enum UiEventType implements Event{
	UiCreated,
	playerCreated,
	networkServerListening,
	networkConnected,
	networkDisconnected,
	networkMoveLeft,
	networkMoveRight,
	networkStopMovingLeft,
	networkStopMovingRight,
	networkJump, playerDestroyed, networkConnectionEstablished;


	@Override
	public String getName() {
		return toString();
	}

}
