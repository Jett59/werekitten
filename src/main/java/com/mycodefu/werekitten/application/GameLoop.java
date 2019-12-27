package com.mycodefu.werekitten.application;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.mycodefu.werekitten.event.PlayerEvent;
import com.mycodefu.werekitten.ui.UI;

import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer{
	//create a queue
	private Queue<PlayerEvent> playerEventQueue = new ConcurrentLinkedQueue<>();
//the UI that the game is running on
private UI ui;
//the move amount constant for the game
public static int CAT_MOVE_AMOUNT = 2;
	@Override
	public void handle(long now) {
		for(int n = 0; n < 10; n++) {
		PlayerEvent playerEvent = playerEventQueue.poll();
		//check if the event is null, if it is, that means that the queue is empty
		if(playerEvent == null) {
			//break out of the for loop
			break;
		}
		//run operations based on the event's type, using a switch statement
		switch (playerEvent.getType()) {
		case moveLeft: {
			//run the moveLeft method on the UI
			ui.moveLeft(CAT_MOVE_AMOUNT);
			break;
		}
		
		case moveRight: {
			//run the moveRight method on the UI
			ui.moveRight(CAT_MOVE_AMOUNT);
			break;
		}
		
		case jump: {
			//run the jump method on the UI
			ui.jump();
			break;
		}
		
		default: 
			throw new IllegalArgumentException("the player event "+playerEvent.getType()+" could not be recognised");
		}
		}

}

public GameLoop(UI ui) {
	//set the ui
	this.ui = ui;
}
}
