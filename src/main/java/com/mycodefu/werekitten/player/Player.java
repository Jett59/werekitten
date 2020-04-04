package com.mycodefu.werekitten.player;

import com.mycodefu.werekitten.animation.Animation;
import com.mycodefu.werekitten.animation.MovementAnimation;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Player {
    public static final double MOTION_PIXEL_RATE = 4d;

    private final String id;
    private Map<AnimationType, Animation> nameToAnimation = new ConcurrentHashMap<>();
    private List<Animation> animations = new ArrayList<>();
    private Group animationGroup;
    private TranslateTransition jump;
    private Animation currentAnimation;
    private double health = 1;
    private Text healthDisplayText = new Text("" + ((int) health) * 999);
    private MovementAnimation[] moveTransitions = new MovementAnimation[2];

    private static boolean DEBUG = true;

    Player(String id, Animation idleRight, Animation idleLeft, Animation walkRight, Animation walkLeft, double jumpAmount, AnimationType initialAnimation, double initialXPosition) {
        this.id = id;
        animations.addAll(List.of(idleRight, idleLeft, walkRight, walkLeft));

        nameToAnimation.put(AnimationType.idleRight, idleRight);
        nameToAnimation.put(AnimationType.idleLeft, idleLeft);
        nameToAnimation.put(AnimationType.walkRight, walkRight);
        nameToAnimation.put(AnimationType.walkLeft, walkLeft);

        animationGroup = new Group(idleRight.getImageView(), idleLeft.getImageView(), walkRight.getImageView(), walkLeft.getImageView(), healthDisplayText);

        jump = new TranslateTransition(Duration.millis(150), animationGroup);
        //jump.interpolatorProperty().set(Interpolator.SPLINE(.1, .1, .7, .7));
        jump.setInterpolator(Interpolator.EASE_OUT);
        jump.setByY(-jumpAmount);
        jump.setAutoReverse(true);
        jump.setCycleCount(2);

        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        double rightBound = screenWidth - walkRight.getImageView().getViewport().getWidth();
        MovementAnimation moveRight = new MovementAnimation(animationGroup, MOTION_PIXEL_RATE, x -> x < rightBound, Duration.INDEFINITE);
        moveTransitions[0] = moveRight;
        MovementAnimation moveLeft = new MovementAnimation(animationGroup, MOTION_PIXEL_RATE * -1, x -> x > 0, Duration.INDEFINITE);
        moveTransitions[1] = moveLeft;

        currentAnimation = playOneAnimation(animations, nameToAnimation.get(initialAnimation));
        animationGroup.setTranslateX(animationGroup.getLayoutX() + initialXPosition);
    }

    public Group getGroup() {
        return animationGroup;
    }

    public void moveLeft(double amount) {
        moveTransitions[1].play();
        currentAnimation = playOneAnimation(animations, nameToAnimation.get(AnimationType.walkLeft));

        if (DEBUG) {
            System.out.println(String.format("Kitten %s moved left by %f.1 to %f.1", id, amount, animationGroup.getLayoutX()));
        }
    }

    public void moveRight(double amount) {
        moveTransitions[0].play();
        currentAnimation = playOneAnimation(animations, nameToAnimation.get(AnimationType.walkRight));

        if (DEBUG) {
            System.out.println(String.format("Kitten %s moved right  by %f.1 to %f.1", id, amount, animationGroup.getLayoutX()));
        }

    }

    public void moveLeftTo(double x) {
        animationGroup.setLayoutX(x);
        //TODO: Check for collisions and undo the move to the rightmost point of the colliding object if so
        currentAnimation = playOneAnimation(animations, nameToAnimation.get(AnimationType.walkLeft));

        if (DEBUG) {
            System.out.println(String.format("Kitten %s moved left to %f.1", id, animationGroup.getLayoutX()));
        }

    }

    public void moveRightTo(double x) {
        animationGroup.setLayoutX(x);
        //TODO: Check for collisions and undo the move to the leftmost point of the colliding object if so
        currentAnimation = playOneAnimation(animations, nameToAnimation.get(AnimationType.walkRight));

        if (DEBUG) {
            System.out.println(String.format("Kitten %s moved right to %f.1", id, animationGroup.getLayoutX()));
        }
    }

    public void jump() {
        jump.play();
    }

    public void stopMovingLeft() {
        moveTransitions[1].stop();
        currentAnimation = playOneAnimation(animations, nameToAnimation.get(AnimationType.idleLeft));
    }

    public void stopMovingRight() {
        moveTransitions[0].stop();
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

    public void moveTo(double x) {
        animationGroup.setLayoutX(x);
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

    public String getId() {
        return id;
    }

    public void dealDamage(double amount) {
        health -= amount;
        healthDisplayText.setText("" + (int) (health * 999d));
    }

    public void recoverHealth(double amount) {
        health += amount;
        healthDisplayText.setText("" + (int) (health * 999d));
    }

    public double getHealth() {
        return health;
    }

    @Override
    public String toString() {
        return "{PlayerId: '" + id + "'}}";
    }
}
