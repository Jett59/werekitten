package com.mycodefu.werekitten.application;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.mycodefu.werekitten.event.NetworkEventType;
import com.mycodefu.werekitten.event.PlayerEventType;
import com.mycodefu.werekitten.ui.UI;

import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {
    //create a queue for player 1
    private Queue<PlayerEventType> player1EventQueue = new ConcurrentLinkedQueue<>();
    //create a queue for player 2
    private Queue<PlayerEventType> player2EventQueue = new ConcurrentLinkedQueue<>();
    //create a queue for player 2
    private Queue<NetworkEventType> networkEventQueue = new ConcurrentLinkedQueue<>();
    //the UI that the game is running on
    private UI ui;
    //the move amount constant for the game
    public static int CAT_MOVE_AMOUNT = 2;

    @Override
    public void handle(long now) {
        //run the event handling 10 times using a for loop
        for (int n = 0; n < 5; n++) {
            runPlayerEvents(player1EventQueue);
            runPlayerEvents(player2EventQueue);
        }

        for (int n = 0; n < 5; n++) {
            runNetworkEvents(networkEventQueue);
        }
    }

    private void runNetworkEvents(Queue<NetworkEventType> eventQueue) {
        //get the NetworkEventType from the front of the eventQueue
        NetworkEventType networkEventType = eventQueue.poll();
        //check if the event is null, if it is, that means that the queue is empty
        if (networkEventType == null) {
            //return
            return;
        }
        //run operations based on the event's type, using a switch statement
        switch (networkEventType) {
            case connected: {
                ui.updateConnectionState(true);
                break;
            }
            case disconnected: {
                ui.updateConnectionState(false);
            }
            break;
        }
    }

    //use the events to run methods
    private void runPlayerEvents(Queue<PlayerEventType> eventQueue) {
        //get the playerEventType from the front of the playerEventqueue
        PlayerEventType playerEventType = eventQueue.poll();
        //check if the event is null, if it is, that means that the queue is empty
        if (playerEventType == null) {
            //return
            return;
        }
        //run operations based on the event's type, using a switch statement
        switch (playerEventType) {
            case moveLeft: {
                //run the moveLeft method on the UI
                ui.getPlayer1().moveLeft(CAT_MOVE_AMOUNT);
                break;
            }

            case moveRight: {
                //run the moveRight method on the UI
                ui.getPlayer1().moveRight(CAT_MOVE_AMOUNT);
                break;
            }

            case jump: {
                //run the jump method on the UI
                ui.getPlayer1().jump();
                break;
            }

            case stopMovingLeft: {
                //run the stopMovingLeft method on the UI
                ui.getPlayer1().stopMovingLeft();
                break;
            }

            case stopMovingRight: {
                //run the stopMovingRight method on the UI
                ui.getPlayer1().stopMovingRight();
                break;
            }

            default:
                throw new IllegalArgumentException("the player event " + playerEventType + " could not be recognised");
        }
    }

    //adds a playerEvent to the PlayerEventQueue
    public void addPlayer1Event(PlayerEventType playerEventType) {
        player1EventQueue.offer(playerEventType);
    }

    public void addPlayer2Event(PlayerEventType playerEventType) {
        player2EventQueue.offer(playerEventType);
    }

    public void addNetworkEvent(NetworkEventType networkEventType) {
        networkEventQueue.offer(networkEventType);
    }

    //constructor for the gameLoop class, passing the ui to execute the player commands
    public GameLoop(UI ui) {
        //set the ui
        this.ui = ui;
    }
}
