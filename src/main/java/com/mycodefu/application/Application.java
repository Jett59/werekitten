package com.mycodefu.application;

import com.mycodefu.animation.Animation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.mycodefu.animation.AnimationCompiler.compileCatAnimation;
import static javafx.animation.Animation.INDEFINITE;

public class Application extends javafx.application.Application{

	public static final int SCREEN_WIDTH = 1024;
	public static final int SCREEN_HEIGHT = 768;

	public static void start(String[] args) {
	Application.launch(args);
}

	@Override
	public void start(Stage stage) throws Exception {

		Animation idleCat = compileCatAnimation("Idle", 10, Duration.seconds(1));
		idleCat.setCycleCount(INDEFINITE);
		double middleX = SCREEN_WIDTH / 2 - idleCat.getImageView().getViewport().getWidth() / 2;
		double middleY = SCREEN_HEIGHT / 2 - idleCat.getImageView().getViewport().getHeight() / 2;
		idleCat.getImageView().setX(middleX);
		idleCat.getImageView().setY(middleY);
		idleCat.play();

		Animation walkingCat = compileCatAnimation("Walk", 10, Duration.seconds(1));
		walkingCat.setCycleCount(INDEFINITE);
		walkingCat.getImageView().setX(middleX);
		walkingCat.getImageView().setY(middleY);
		walkingCat.getImageView().setVisible(false);

		Group g = new Group(idleCat.getImageView(), walkingCat.getImageView());

		Scene s = new Scene(g);

		stage.setScene(s);
		stage.setWidth(SCREEN_WIDTH);
		stage.setHeight(SCREEN_HEIGHT);
		stage.setOnCloseRequest(e->{
			System.exit(0);
		});
		stage.setTitle("werekitten");

		stage.show();

		final AtomicBoolean idle = new AtomicBoolean(true);
		stage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
			if (keyEvent.getCode() == KeyCode.RIGHT && idle.get()) {
				idleCat.stop();
				idleCat.getImageView().setVisible(false);
				walkingCat.getImageView().setVisible(true);
				walkingCat.play();
				idle.set(false);
			}
		});
		stage.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
			if (keyEvent.getCode() == KeyCode.RIGHT && !idle.get()) {
				walkingCat.stop();
				walkingCat.getImageView().setVisible(false);
				idleCat.getImageView().setVisible(true);
				idleCat.play();
				idle.set(true);
			}
		});
	}

}
