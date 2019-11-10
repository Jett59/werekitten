package com.mycodefu.application;

import com.mycodefu.animation.Animation;
import com.mycodefu.backgroundObjects.BackgroundObject;
import com.mycodefu.backgroundObjects.BackgroundObjectBuilder;
import com.mycodefu.backgroundObjects.NodeObject;
import com.mycodefu.slide.SlideBackground;
import com.mycodefu.sound.MusicPlayer;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mycodefu.animation.AnimationCompiler.compileAnimation;
import static javafx.animation.Animation.INDEFINITE;

public class Application extends javafx.application.Application{

	public static final int SCREEN_WIDTH = 1024;
	public static final int SCREEN_HEIGHT = 768;
	public static final double CAT_SCALE = 0.5d;
	public static final double SHREW_SCALE = 0.5d;
public static final int catMoveAnimationDurationMilis = 100;

	public static void start(String[] args) {
	Application.launch(args);
}

	@Override
	public void start(Stage stage) {
		try {
			Animation idleRightCat = compileAnimation("characters/cat", "Idle", 10, Duration.seconds(1), false, CAT_SCALE);
			idleRightCat.setCycleCount(INDEFINITE);
			double middleX = SCREEN_WIDTH / 2 - idleRightCat.getImageView().getViewport().getWidth() / 2;
			double middleY = SCREEN_HEIGHT / 2 - idleRightCat.getImageView().getViewport().getHeight() / 2;
			idleRightCat.getImageView().setX(middleX);
			idleRightCat.getImageView().setY(middleY);
			idleRightCat.play();

			Animation idleLeftCat = compileAnimation("characters/cat", "Idle", 10, Duration.seconds(1), true, CAT_SCALE);
			idleLeftCat.setCycleCount(INDEFINITE);
			idleLeftCat.getImageView().setX(middleX);
			idleLeftCat.getImageView().setY(middleY);
			idleLeftCat.getImageView().setVisible(false);

			Animation walkingRightCat = compileAnimation("characters/cat", "Walk", 10, Duration.seconds(1), false, CAT_SCALE);
			walkingRightCat.setCycleCount(INDEFINITE);
			walkingRightCat.getImageView().setX(middleX);
			walkingRightCat.getImageView().setY(middleY);
			walkingRightCat.getImageView().setVisible(false);

			Animation walkingLeftCat = compileAnimation("characters/cat", "Walk", 10, Duration.seconds(1), true, CAT_SCALE);
			walkingLeftCat.setCycleCount(INDEFINITE);
			walkingLeftCat.getImageView().setX(middleX);
			walkingLeftCat.getImageView().setY(middleY);
			walkingLeftCat.getImageView().setVisible(false);

			Animation shrewRight = compileAnimation("characters/shrew", "Idle", 2, Duration.millis(250), false, SHREW_SCALE);
			shrewRight.setCycleCount(INDEFINITE);
			double shrewX = SCREEN_WIDTH / 2 - shrewRight.getImageView().getViewport().getWidth() - idleRightCat.getImageView().getViewport().getWidth() - 50;
			double shrewY = middleY + idleRightCat.getImageView().getViewport().getHeight() - shrewRight.getImageView().getViewport().getHeight();
			shrewRight.getImageView().setX(shrewX);
			shrewRight.getImageView().setY(shrewY);
			shrewRight.play();


			List<Animation> catAnimations = List.of(idleLeftCat, idleRightCat, walkingLeftCat, walkingRightCat);
			List<Node> catAnimationImageViews = catAnimations.stream().map(Animation::getImageView).collect(Collectors.toList());

			Group catAnimationGroup = new Group(catAnimationImageViews);

			TranslateTransition jump = new TranslateTransition(Duration.millis(150), catAnimationGroup);
			jump.interpolatorProperty().set(Interpolator.SPLINE(.1, .1, .7, .7));
			jump.setByY(-125);
			jump.setAutoReverse(true);
			jump.setCycleCount(2);

			NodeObject objects = BackgroundObjectBuilder.buildAll().getBackgroundObjectByName("Crate");
			List<NodeObject> nodeObjectList = List.of(objects, shrewRight.asBackgroundObject("shrew right idol"));
			SlideBackground slide = SlideBackground.withNodes(nodeObjectList);
Group backgroundGroup = slide.getAsGroup();

			Group combinedGroup = new Group(catAnimationGroup, backgroundGroup);
			Scene s = new Scene(combinedGroup);

			stage.setScene(s);
			stage.setWidth(SCREEN_WIDTH);
			stage.setHeight(SCREEN_HEIGHT);
			stage.setOnCloseRequest(e->{
				System.exit(0);
			});
			stage.setTitle("werekitten");

			stage.show();
MusicPlayer.playMain();

			stage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
				if (keyEvent.getCode() == KeyCode.RIGHT) {
					playOneAnimation(catAnimations, walkingRightCat);
					slide.moveX(-10);
				} else if (keyEvent.getCode() == KeyCode.LEFT) {
					playOneAnimation(catAnimations, walkingLeftCat);
					slide.moveX(10);
				} else if (keyEvent.getCode()==KeyCode.SPACE) {
					jump.play();
				}
			});
			stage.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
				if (keyEvent.getCode() == KeyCode.RIGHT) {
					playOneAnimation(catAnimations, idleRightCat);
				} else if (keyEvent.getCode() == KeyCode.LEFT) {
					playOneAnimation(catAnimations, idleLeftCat);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();

			System.exit(1);
		}
	}

	private void playOneAnimation(List<Animation> allAnimations, Animation animationToPlay) {
		for (Animation animation : allAnimations) {
			if (animation == animationToPlay) {
				animation.getImageView().setVisible(true);
				animation.play();
			} else {
				animation.stop();
				animation.getImageView().setVisible(false);
			}
		}
	}

}
