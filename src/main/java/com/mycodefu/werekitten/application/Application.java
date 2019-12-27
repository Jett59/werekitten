package com.mycodefu.werekitten.application;

import com.mycodefu.werekitten.animation.Animation;
import com.mycodefu.werekitten.backgroundObjects.BackgroundObjectBuilder;
import com.mycodefu.werekitten.backgroundObjects.NodeObject;
import com.mycodefu.werekitten.level.LevelReader;
import com.mycodefu.werekitten.level.data.Level;
import com.mycodefu.werekitten.slide.LayerGroup;
import com.mycodefu.werekitten.slide.SlideBackground;
import com.mycodefu.werekitten.sound.MusicPlayer;
import com.mycodefu.werekitten.ui.UI;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.mycodefu.werekitten.animation.AnimationCompiler;
import static javafx.animation.Animation.INDEFINITE;

public class Application extends javafx.application.Application implements UI{

    public static final int SCREEN_WIDTH = 1024;
    public static final int SCREEN_HEIGHT = 768;
    public static final double CAT_SCALE = 0.5d;
    public static final double SHREW_SCALE = 0.2d;

    public static void start(String[] args) {
        Application.launch(args);
    }
    
    @Override
    public void start(Stage stage) {
       try {
    	   stage.show();
           stage.setWidth(SCREEN_WIDTH);
           stage.setHeight(SCREEN_HEIGHT);
           stage.setOnCloseRequest(e -> {
               System.exit(0);
           });
           stage.setTitle("werekitten");
           
            AnimationCompiler animationCompiler = new AnimationCompiler();
            BackgroundObjectBuilder backgroundObjectBuilder = new BackgroundObjectBuilder(new AnimationCompiler());

        	
        	Animation idleRightCat = animationCompiler.compileAnimation("cat", "Idle", 10, Duration.seconds(1), false, CAT_SCALE);
            idleRightCat.setCycleCount(INDEFINITE);
            double middleX = SCREEN_WIDTH / 2 - idleRightCat.getImageView().getViewport().getWidth() / 2;
            double middleY = (SCREEN_HEIGHT / 2) - (idleRightCat.getImageView().getViewport().getHeight() / 2);
            idleRightCat.getImageView().setX(middleX);
            idleRightCat.getImageView().setY(middleY);
            idleRightCat.play();

            

            Animation idleLeftCat = animationCompiler.compileAnimation("cat", "Idle", 10, Duration.seconds(1), true, CAT_SCALE);
            idleLeftCat.setCycleCount(INDEFINITE);
            idleLeftCat.getImageView().setX(middleX);
            idleLeftCat.getImageView().setY(middleY);
            idleLeftCat.getImageView().setVisible(false);

            Animation walkingRightCat = animationCompiler.compileAnimation("cat", "Walk", 10, Duration.seconds(1), false, CAT_SCALE);
            walkingRightCat.setCycleCount(INDEFINITE);
            walkingRightCat.getImageView().setX(middleX);
            walkingRightCat.getImageView().setY(middleY);
            walkingRightCat.getImageView().setVisible(false);

            Animation walkingLeftCat = animationCompiler.compileAnimation("cat", "Walk", 10, Duration.seconds(1), true, CAT_SCALE);
            walkingLeftCat.setCycleCount(INDEFINITE);
            walkingLeftCat.getImageView().setX(middleX);
            walkingLeftCat.getImageView().setY(middleY);
            walkingLeftCat.getImageView().setVisible(false);

            Animation shrewRight = animationCompiler.compileAnimation("shadyshrew", "Idle", 2, Duration.millis(250), false, SHREW_SCALE);
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

            List<NodeObject> possibleCollisions = new ArrayList<>();

            Level defaultLevel = LevelReader.read("/level.wkl");
            List<LayerGroup> layerGroups = defaultLevel.getLayers().stream().map(layer -> {

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

            stage.setScene(s);



            MusicPlayer.playLevel();


            stage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
                
            });
            stage.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
            	
            });
            
        } catch (Exception e) {
            e.printStackTrace();

            System.exit(1);
        }
    }

    

	@Override
	public void jump() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveLeft(int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveRight(int amount) {
		// TODO Auto-generated method stub
		
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
