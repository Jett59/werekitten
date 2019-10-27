package com.mycodefu.application;

import com.mycodefu.animation.Animation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import static com.mycodefu.animation.AnimationCompiler.compileCatAnimation;
import static javafx.animation.Animation.INDEFINITE;

public class Application extends javafx.application.Application{
public static void start(String[] args) {
	Application.launch(args);
}
	@Override
	public void start(Stage stage) throws Exception {

		Animation walkingCat = compileCatAnimation("Walk", 10, Duration.seconds(1));
		walkingCat.setCycleCount(INDEFINITE);
		walkingCat.getImageView().setX(-75d);
		walkingCat.play();

		Animation runningCat = compileCatAnimation("Run", 8, Duration.seconds(1));
		runningCat.setCycleCount(INDEFINITE);
		runningCat.getImageView().setX(175d);
		runningCat.play();

		Animation deadCat = compileCatAnimation("Dead", 10, Duration.seconds(1));
		deadCat.setCycleCount(INDEFINITE);
		deadCat.getImageView().setX(575d);
		deadCat.play();

		Group g = new Group(walkingCat.getImageView(), runningCat.getImageView(), deadCat.getImageView());

		Scene s = new Scene(g);

		stage.setScene(s);
		stage.setWidth(1024);
		stage.setHeight(768);
		stage.setOnCloseRequest(e->{
			System.exit(0);
		});
		stage.setTitle("werekitten");

		stage.show();


	}

}
