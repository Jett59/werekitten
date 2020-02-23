package com.mycodefu.werekitten.player;

import com.mycodefu.werekitten.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.DataFormatException;

public class Player {
    private final String id;
    private final double jumpAmount;
    private final double initialXPosition;
    private Map<AnimationType, Animation> nameToAnimation = new ConcurrentHashMap<>();
    private List<Animation> animations = new ArrayList<>();
    private Group animationGroup;
    private TranslateTransition jump;
    private Animation currentAnimation;

    private static boolean DEBUG = false;

    Player(String id, Animation idleRight, Animation idleLeft, Animation walkRight, Animation walkLeft, double jumpAmount, AnimationType initialAnimation, double initialXPosition) {
        this.id = id;
        this.jumpAmount = jumpAmount;
        this.initialXPosition = initialXPosition;
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

        if (DEBUG){
            System.out.println(String.format("Kitten %s moved left by %f.1 to %f.1", id, amount, animationGroup.getTranslateX()));
        }
    }

    public void moveRight(double amount) {
        animationGroup.setTranslateX(animationGroup.getTranslateX() + amount);
        //TODO: Check for collisions and undo the move to the leftmost point of the colliding object if so
        currentAnimation = playOneAnimation(animations, nameToAnimation.get(AnimationType.walkRight));

        if (DEBUG){
            System.out.println(String.format("Kitten %s moved right  by %f.1 to %f.1", id, amount, animationGroup.getTranslateX()));
        }

    }

    public void moveLeftTo(double x) {    	
        animationGroup.setTranslateX(x);
        //TODO: Check for collisions and undo the move to the rightmost point of the colliding object if so
        currentAnimation = playOneAnimation(animations, nameToAnimation.get(AnimationType.walkLeft));

        if (DEBUG){
            System.out.println(String.format("Kitten %s moved left to %f.1", id, animationGroup.getTranslateX()));
        }

    }

    public void moveRightTo(double x) {    	
    	System.out.println("moving to "+x);
        animationGroup.setTranslateX(x);
        //TODO: Check for collisions and undo the move to the leftmost point of the colliding object if so
        currentAnimation = playOneAnimation(animations, nameToAnimation.get(AnimationType.walkRight));

        if (DEBUG){
            System.out.println(String.format("Kitten %s moved right to %f.1", id, animationGroup.getTranslateX()));
        }
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
    
    public Shape getCurrentShape() {
    	return currentAnimation.getCurrentShape();
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
