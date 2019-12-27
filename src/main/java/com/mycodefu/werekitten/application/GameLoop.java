package com.mycodefu.werekitten.application;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.mycodefu.werekitten.event.PlayerEventType;
import com.mycodefu.werekitten.ui.UI;

import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer{
	//create a queue
	private Queue<PlayerEventType> playerEventQueue = new ConcurrentLinkedQueue<>();
//the UI that the game is running on
private UI ui;
//the move amount constant for the game
public static int CAT_MOVE_AMOUNT = 2;
	@Override
	public void handle(long now) {
		//run the event handling 10 times using a for loop
		for(int n = 0; n < 10; n++) {
			//get the playerEventType from the front of the queue
		PlayerEventType playerEventType = playerEventQueue.poll();
		//check if the event is null, if it is, that means that the queue is empty
		if(playerEventType == null) {
			//break out of the for loop
			break;
		}
		//run operations based on the event's type, using a switch statement
		switch (playerEventType) {
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
		
		case stopMovingLeft: {
			//run the stopMovingLeft method on the UI
			ui.stopMovingLeft();
			break;
		}
		
		case stopMovingRight: {
			//run the stopMovingRight method on the UI
			ui.stopMovingRight();
			break;
		}
		
		default: 
			throw new IllegalArgumentException("the player event "+playerEventType+" could not be recognised");
		}
		}

}

//adds a playerEvent to the PlayerEventQueue
public void addPlayerEvent(PlayerEventType playerEventType) {
	playerEventQueue.offer(playerEventType);
}

//constructor for the gameLoop class, passing the ui to execute the player commands
public GameLoop(UI ui) {
	//set the ui
	this.ui = ui;
}
}
