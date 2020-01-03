package com.mycodefu.werekitten.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mycodefu.werekitten.animation.Animation;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.util.Duration;

public class Player {
private Map<String, Animation> nameToAnimation = new ConcurrentHashMap<>();
private List<Animation> animations = new ArrayList<>();
private Group animationGroup;
private TranslateTransition jump;

Player(Animation idleRight, Animation idleLeft, Animation walkRight, Animation walkLeft, int jumpAmount) {
	animations.addAll(List.of(idleRight, idleLeft, walkRight, walkLeft));
	
	nameToAnimation.put("idleRight", idleRight);
	nameToAnimation.put("idleLeft", idleLeft);
	nameToAnimation.put("walkRight", walkRight);
	nameToAnimation.put("walkLeft", walkLeft);
	
	animationGroup = new Group(idleRight.getImageView(), idleLeft.getImageView(), walkRight.getImageView(), walkLeft.getImageView());
	
	jump = new TranslateTransition(Duration.millis(150), animationGroup);
    jump.interpolatorProperty().set(Interpolator.SPLINE(.1, .1, .7, .7));
    jump.setByY(-jumpAmount);
    jump.setAutoReverse(true);
    jump.setCycleCount(2);
}

public Group getGroup() {
	return animationGroup;
}

public void moveLeft(int amount) {
	animationGroup.setTranslateX(animationGroup.getTranslateX()-amount);
	playOneAnimation(animations, nameToAnimation.get("walkLeft"));
}

public void moveRight(int amount) {
	animationGroup.setTranslateX(animationGroup.getTranslateX()+amount);
	playOneAnimation(animations, nameToAnimation.get("walkRight"));
}

public void jump() {
	jump.play();
}

public void stopMovingLeft() {
	playOneAnimation(animations, nameToAnimation.get("idleLeft"));
}

public void stopMovingRight() {
	playOneAnimation(animations, nameToAnimation.get("idleRight"));
}

private Animation playOneAnimation(List<Animation> allAnimations, Animation animationToPlay) {
    for (Animation animation : allAnimations) {
        if (animation == animationToPlay) {
            animation.getImageView().setVisible(true);
            animation.play();
        } else {
            animation.stop();
            animation.getImageView().setVisible(false);
        }
    }
    return animationToPlay;
}

}
