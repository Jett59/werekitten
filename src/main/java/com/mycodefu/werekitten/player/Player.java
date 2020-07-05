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

import static com.mycodefu.werekitten.animation.MovementAnimation.MovementDirection.Left;
import static com.mycodefu.werekitten.animation.MovementAnimation.MovementDirection.Right;

public class Player {
    private final String id;
    private  Map<AnimationType, Animation> nameToAnimation = new ConcurrentHashMap<>();
    private List<Animation> animations = new ArrayList<>();
    private Group animationGroup;
    private TranslateTransition jump;
    private Animation currentAnimation;
    private double health = 1;
    private Text healthDisplayText = new Text("" + ((int) health) * 999);
    private final MovementAnimation moveTransition;

    private static boolean DEBUG = true;

    Player(String id, Animation idleRight, Animation idleLeft, Animation walkRight, Animation walkLeft, double jumpAmount, AnimationType initialAnimation, double initialXPosition, double speed) {
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
        double rightBound = screenWidth - walkRight.getCurrentShape().maxWidth(-1);
        double leftBound = walkLeft.getCurrentShape().maxWidth(-1)-walkLeft.getImageView().getViewport().getWidth();
        moveTransition = new MovementAnimation(animationGroup, speed, MovementAnimation.MovementDirection.Left, rightBound, leftBound, Duration.INDEFINITE);

        currentAnimation = playOneAnimation(animations, nameToAnimation.get(initialAnimation));
        animationGroup.setTranslateX(animationGroup.getLayoutX() + initialXPosition);
    }

    public Group getGroup() {
        return animationGroup;
    }

    public void moveLeft(double xSync) {
        if (!Double.isNaN(xSync)) {
            moveTransition.syncX(xSync);
        }
        moveTransition.stop();
        moveTransition.setDirection(Left);
        if (!moveTransition.getStatus().equals(javafx.animation.Animation.Status.RUNNING)) {
            moveTransition.play();
        }
        this.currentAnimation = playOneAnimation(animations, nameToAnimation.get(AnimationType.walkLeft));

        if (DEBUG) {
            System.out.println(String.format("Kitten %s moved left by %f.1 to %f.1", id, xSync, animationGroup.getLayoutX()));
        }
    }

    public void moveRight(double xSync) {
        if (!Double.isNaN(xSync)) {
            moveTransition.syncX(xSync);
        }
        moveTransition.stop();
        moveTransition.setDirection(Right);
        if (!moveTransition.getStatus().equals(javafx.animation.Animation.Status.RUNNING)) {
            moveTransition.play();
        }
        this.currentAnimation = playOneAnimation(animations, nameToAnimation.get(AnimationType.walkRight));

        if (DEBUG) {
            System.out.println(String.format("Kitten %s moved right  by %f.1 to %f.1", id, xSync, animationGroup.getLayoutX()));
        }
    }


    public void jump() {
        jump.play();
    }

    public void stopMovingLeft(double xSync) {
        if (!Double.isNaN(xSync)) {
            moveTransition.syncX(xSync);
        }
        if(moveTransition.getDirection().equals(MovementAnimation.MovementDirection.Left)) {
        moveTransition.stop();
        currentAnimation = playOneAnimation(animations, nameToAnimation.get(AnimationType.idleLeft));
        }
    }

    public void stopMovingRight(double xSync) {
        if (!Double.isNaN(xSync)) {
            moveTransition.syncX(xSync);
        }
        if(moveTransition.getDirection().equals(MovementAnimation.MovementDirection.Right)) {
        moveTransition.stop();
        currentAnimation = playOneAnimation(animations, nameToAnimation.get(AnimationType.idleRight));
        }
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

    public double getX() {
        return animationGroup.getTranslateX();
    }

    @Override
    public String toString() {
        return "{PlayerId: '" + id + "'}}";
    }
}
