package com.mycodefu.werekitten.player;

import com.mycodefu.werekitten.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Player {
    private Map<AnimationType, Animation> nameToAnimation = new ConcurrentHashMap<>();
    private List<Animation> animations = new ArrayList<>();
    private Group animationGroup;
    private TranslateTransition jump;
    private Animation currentAnimation;

    Player(Animation idleRight, Animation idleLeft, Animation walkRight, Animation walkLeft, double jumpAmount, AnimationType initialAnimation, double initialXPosition) {
        animations.addAll(List.of(idleRight, idleLeft, walkRight, walkLeft));

        nameToAnimation.put(AnimationType.idleRight, idleRight);
        nameToAnimation.put(AnimationType.idleLeft, idleLeft);
        nameToAnimation.put(AnimationType.walkRight, walkRight);
        nameToAnimation.put(AnimationType.walkLeft, walkLeft);

        animationGroup = new Group(idleRight.getImageView(), idleLeft.getImageView(), walkRight.getImageView(), walkLeft.getImageView());

        jump = new TranslateTransition(Duration.millis(150), animationGroup);
        //jump.interpolatorProperty().set(Interpolator.SPLINE(.1, .1, .7, .7));
        jump.setInterpolator(Interpolator.EASE_OUT);
        jump.setByY(-jumpAmount);
        jump.setAutoReverse(true);
        jump.setCycleCount(2);

        currentAnimation = playOneAnimation(animations, nameToAnimation.get(initialAnimation));
        animationGroup.setTranslateX(animationGroup.getTranslateX() + initialXPosition);
    }

    public Group getGroup() {
        return animationGroup;
    }

    public void moveLeft(double amount) {
        animationGroup.setTranslateX(animationGroup.getTranslateX() - amount);
        //TODO: Check for collisions and undo the move to the rightmost point of the colliding object if so
        currentAnimation = playOneAnimation(animations, nameToAnimation.get(AnimationType.walkLeft));
    }

    public void moveRight(double amount) {
        animationGroup.setTranslateX(animationGroup.getTranslateX() + amount);
        //TODO: Check for collisions and undo the move to the leftmost point of the colliding object if so
        currentAnimation = playOneAnimation(animations, nameToAnimation.get(AnimationType.walkRight));
    }

    public void jump() {
        jump.play();
    }

    public void stopMovingLeft() {
        currentAnimation = playOneAnimation(animations, nameToAnimation.get(AnimationType.idleLeft));
    }

    public void stopMovingRight() {
        currentAnimation = playOneAnimation(animations, nameToAnimation.get(AnimationType.idleRight));
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

    public enum AnimationType {
        idleRight,
        idleLeft,
        walkRight,
        walkLeft
    }

    public Animation getCurrentAnimation() {
        return currentAnimation;
    }
}
