package com.mycodefu.werekitten.backgroundObjects;


import com.mycodefu.werekitten.animation.Animation;
import com.mycodefu.werekitten.animation.AnimationCompiler;
import com.mycodefu.werekitten.image.ImageHelper;

import com.mycodefu.werekitten.level.data.Element;

import javafx.animation.Interpolator;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static javafx.animation.Animation.INDEFINITE;

import java.awt.image.BufferedImage;

public class BackgroundObjectBuilder {

private AnimationCompiler animationCompiler;

public BackgroundObjectBuilder(AnimationCompiler animationCompiler) {
	this.animationCompiler = animationCompiler;
}

    private String toResourceDir(String from) {
        return "/background/freetileset/png/Object/" + from + ".png";
    }

    public NodeObject build(Element element) {
        NodeObject result;
        switch (element.getType()) {
            case Animation: {
                int dotIndex = element.getName().indexOf(".");
                String character = element.getName().substring(0, dotIndex);
                String animation = element.getName().substring(dotIndex + 1);

                Animation animat;
                if (element.getSize() != null) {
                	if(element.getSize().getHeight() > 0 || element.getSize().getWidth() > 0) {
                	if(element.getSize().getWidth() > 0 && element.getSize().getHeight() > 0) {
                		throw new IllegalArgumentException("the size parameter specified on element "+element.getName()+" cannot have both width and height specified at one time");
                	}
                    String widthOrHeight;
                    int size;
                    if (element.getSize().getHeight() > 0) {
                        widthOrHeight = "height";
                        size = element.getSize().getHeight();
                    } else {
                        widthOrHeight = "width";
                        size = element.getSize().getWidth();
                    }
                    animat = animationCompiler.compileAnimation(character, animation, element.getAnimationConfig().getFramesInAnimation(), Duration.millis(element.getAnimationConfig().getDurationMillis()), element.getAnimationConfig().getReversed(), size, widthOrHeight);
                	}else {
                		throw new IllegalArgumentException("the size parameter on element "+element.getName()+" has neither width or height specified");
                	}
                } else {
                    animat = animationCompiler.compileAnimation(character, animation, element.getAnimationConfig().getFramesInAnimation(), Duration.millis(element.getAnimationConfig().getDurationMillis()), element.getAnimationConfig().getReversed());
                }

                animat.getImageView().setX(element.getLocation().getX());
                animat.getImageView().setY(element.getLocation().getY());

                BackgroundAnimationObject animatedBackgroundObject = animat.asBackgroundObject(element.getName());


                animatedBackgroundObject.getAnimation().setCycleCount(INDEFINITE);
                animatedBackgroundObject.getAnimation().setInterpolator(Interpolator.LINEAR);
                animatedBackgroundObject.getAnimation().play();
                result = animatedBackgroundObject;
                break;
            }

            case Image: {
                BufferedImage bufferedImage = ImageHelper.readBufferedImage(toResourceDir(element.getName()));
                if(element.getSize() != null) {
                if(element.getSize().getHeight() > 0 && element.getSize().getWidth() > 0) {
                	throw new IllegalArgumentException("the size parameter on element "+element.getName()+" cannot have both width and height specified at one time");
                }else if(element.getSize().getHeight() < 1 && element.getSize().getWidth() < 1) {
                	throw new IllegalArgumentException("the size parameter specified on element "+element.getName()+" must have either width or height specified");
                }else {
                	double scale =
                			element.getSize().getHeight() > 0 ?
                					(double)element.getSize().getHeight()/(double)bufferedImage.getHeight() :
                				(double)element.getSize().getWidth()/(double)bufferedImage.getWidth();
                	bufferedImage = ImageHelper.scaleImage(bufferedImage, scale);
                }
                }
                Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
                BackgroundImageObject backgroundImageObject = new BackgroundImageObject(fxImage, element.getName());
                backgroundImageObject.getImageView().setX(element.getLocation().getX());
                backgroundImageObject.getImageView().setY(element.getLocation().getY());
                result = backgroundImageObject;

                break;
            }
            case Rectangle: {
                result = createBackgroundShapeFromElement(element);
                break;
            }
            
            case Player: {
            	//empty node
                final Group group = new Group();
            	result = new NodeObject() {

					@Override
					public String getName() {
						return element.getName();
					}

					@Override
					public Node getNode() {
                        return group;
					}
            		
            	};
            	break;
            }

            default:
                throw new IllegalArgumentException(String.format("Unknown element type '%s'.", element.getType()));
        }
        return result;
    }

    public BackgroundShapeObject createBackgroundShapeFromElement(Element element) {
        if (element.getSize() == null) {
            throw new IllegalArgumentException(String.format("No size specified on shape element named '%s'.", element.getName()));
        }
        if (element.getLocation() == null) {
            throw new IllegalArgumentException(String.format("No location specified on shape element named '%s'.", element.getName()));
        }
        Rectangle rectangle = new Rectangle(
                element.getLocation().getX(),
                element.getLocation().getY(),
                element.getSize().getWidth(),
                element.getSize().getHeight()
        );
        if (element.getFillColor() != null) {
            rectangle.setFill(new Color(
                    element.getFillColor().getRed(),
                    element.getFillColor().getGreen(),
                    element.getFillColor().getBlue(),
                    element.getFillColor().getOpacity()
            ));
        } else {
            rectangle.setFill(Color.TRANSPARENT);
        }
        rectangle.setStrokeWidth(element.getStrokeWidth());
        if (element.getStrokeColor() != null) {
            rectangle.setStroke(
                    new Color(
                            element.getStrokeColor().getRed(),
                            element.getStrokeColor().getGreen(),
                            element.getStrokeColor().getBlue(),
                            element.getStrokeColor().getOpacity()
                    ));
        } else {
            rectangle.setStroke(Color.BLACK);
        }
        BackgroundShapeObject result = new BackgroundShapeObject(element.getName(), rectangle);
        return result;
    }

}
