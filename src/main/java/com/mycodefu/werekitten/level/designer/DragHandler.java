package com.mycodefu.werekitten.level.designer;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class DragHandler implements EventHandler<MouseEvent> {

	@Override
	public void handle(MouseEvent event) {
		System.out.printf("drag detected at x %f and y %f\n", event.getSceneX(), event.getSceneY());
	}

}
