package com.mycodefu.werekitten.ui;

import com.mycodefu.werekitten.animation.Animation;
import com.mycodefu.werekitten.animation.AnimationCompiler;
import com.mycodefu.werekitten.backgroundObjects.BackgroundObjectBuilder;
import com.mycodefu.werekitten.backgroundObjects.NodeObject;
import com.mycodefu.werekitten.level.LevelReader;
import com.mycodefu.werekitten.level.data.Level;
import com.mycodefu.werekitten.slide.LayerGroup;
import com.mycodefu.werekitten.slide.SlideBackground;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static javafx.animation.Animation.INDEFINITE;

public class GameUI implements UI {
    public static final double CAT_SCALE = 0.5d;
    public static final double SHREW_SCALE = 0.2d;

    Animation idleRightCat;
    Animation idleLeftCat;
    Animation walkingRightCat;
    Animation walkingLeftCat;
    Animation shrewRight;
    List<Animation> catAnimations;
    TranslateTransition jump;

    public Scene getScene(int screenWidth, int screenHeight) {
        try {
            AnimationCompiler animationCompiler = new AnimationCompiler();
            BackgroundObjectBuilder backgroundObjectBuilder = new BackgroundObjectBuilder(new AnimationCompiler());


            idleRightCat = animationCompiler.compileAnimation("cat", "Idle", 10, Duration.seconds(1), false, CAT_SCALE);
            idleRightCat.setCycleCount(INDEFINITE);
            double middleX = screenWidth / 2 - idleRightCat.getImageView().getViewport().getWidth() / 2;
            double middleY = (screenHeight / 2) - (idleRightCat.getImageView().getViewport().getHeight() / 2);
            idleRightCat.getImageView().setX(middleX);
            idleRightCat.getImageView().setY(middleY);
            idleRightCat.play();


            idleLeftCat = animationCompiler.compileAnimation("cat", "Idle", 10, Duration.seconds(1), true, CAT_SCALE);
            idleLeftCat.setCycleCount(INDEFINITE);
            idleLeftCat.getImageView().setX(middleX);
            idleLeftCat.getImageView().setY(middleY);
            idleLeftCat.getImageView().setVisible(false);

            walkingRightCat = animationCompiler.compileAnimation("cat", "Walk", 10, Duration.seconds(1), false, CAT_SCALE);
            walkingRightCat.setCycleCount(INDEFINITE);
            walkingRightCat.getImageView().setX(middleX);
            walkingRightCat.getImageView().setY(middleY);
            walkingRightCat.getImageView().setVisible(false);

            walkingLeftCat = animationCompiler.compileAnimation("cat", "Walk", 10, Duration.seconds(1), true, CAT_SCALE);
            walkingLeftCat.setCycleCount(INDEFINITE);
            walkingLeftCat.getImageView().setX(middleX);
            walkingLeftCat.getImageView().setY(middleY);
            walkingLeftCat.getImageView().setVisible(false);

            shrewRight = animationCompiler.compileAnimation("shadyshrew", "Idle", 2, Duration.millis(250), false, SHREW_SCALE);
            shrewRight.setCycleCount(INDEFINITE);
            double shrewX = screenWidth / 2 - shrewRight.getImageView().getViewport().getWidth() - idleRightCat.getImageView().getViewport().getWidth() - 50;
            double shrewY = middleY + idleRightCat.getImageView().getViewport().getHeight() - shrewRight.getImageView().getViewport().getHeight();
            shrewRight.getImageView().setX(shrewX);
            shrewRight.getImageView().setY(shrewY);
            shrewRight.play();


            catAnimations = List.of(idleLeftCat, idleRightCat, walkingLeftCat, walkingRightCat);
            List<Node> catAnimationImageViews = catAnimations.stream().map(Animation::getImageView).collect(Collectors.toList());

            Group catAnimationGroup = new Group(catAnimationImageViews);

            jump = new TranslateTransition(Duration.millis(150), catAnimationGroup);
            jump.interpolatorProperty().set(Interpolator.SPLINE(.1, .1, .7, .7));
            jump.setByY(-125);
            jump.setAutoReverse(true);
            jump.setCycleCount(2);

            List<NodeObject> possibleCollisions = new ArrayList<>();

            Level defaultLevel = LevelReader.read("/level.wkl");
            List<LayerGroup> layerGroups = defaultLevel.getLayers().stream()
                    .map(layer -> {
                        List<NodeObject> elements = layer.getElements().stream().map(backgroundElement -> {
                            NodeObject nodeObject = backgroundObjectBuilder.build(backgroundElement);
                            return nodeObject;
                        }).collect(Collectors.toList());

                        if (layer.getDepth() >= 0) {
                            possibleCollisions.addAll(elements);
                        }

                        Group group = new Group(elements.stream().map(NodeObject::getNode).collect(Collectors.toList()));
                        return new LayerGroup(layer.getName(), group, layer.getScrollSpeed(), layer.getDepth());
                    })
                    .sorted(Comparator.comparingInt(LayerGroup::getDepth))
                    .collect(Collectors.toList());

            Group combinedGroup = new Group();
            boolean addedCat = false;

            SlideBackground slide = SlideBackground.empty();
            for (LayerGroup layerGroup : layerGroups) {
                if (layerGroup.getDepth() >= 0 && !addedCat) {
                    combinedGroup.getChildren().add(catAnimationGroup);
                    addedCat = true;
                }
                slide.addLayerGroup(layerGroup);

                combinedGroup.getChildren().add(layerGroup.getGroup());
            }

            if (!addedCat) {
                combinedGroup.getChildren().add(catAnimationGroup);
            }

            Scene s = new Scene(combinedGroup);
            return s;


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to create game UI scene", e);
        }
    }


    @Override
    public void jump() {
        jump.play();
    }

    @Override
    public void moveLeft(int amount) {
        playOneAnimation(catAnimations, walkingLeftCat);
    }

    @Override
    public void moveRight(int amount) {
        playOneAnimation(catAnimations, walkingRightCat);
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
