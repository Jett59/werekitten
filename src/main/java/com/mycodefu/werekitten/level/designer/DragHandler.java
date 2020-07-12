package com.mycodefu.werekitten.level.designer;

import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class DragHandler implements EventHandler<MouseEvent> {

	@Override
	public void handle(MouseEvent event) {
		System.out.printf("drag detected at x %f and y %f\n", event.getSceneX(), event.getSceneY());
		EventTarget target = event.getTarget();
		if (target instanceof ImageView){
			ImageView imageViewTarget = (ImageView) target;
			System.out.println("ImageView: " + imageViewTarget);
			imageViewTarget.setX(event.getX());
			imageViewTarget.setY(event.getY());
		} else {
			System.out.println(target);
		}

	}

}
