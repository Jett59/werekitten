package com.mycodefu.werekitten.backgroundObjects;


import com.mycodefu.werekitten.image.ImageHelper;

import com.mycodefu.werekitten.level.data.Element;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BackgroundObjectBuilder {
    private static String toResourceDir(String from) {
        return "/background/freetileset/png/Object/" + from + ".png";
    }

    public static NodeObject build(Element element) {
        NodeObject result;
        switch (element.getType()) {

            //TODO: Support BackgroundAnimationObject

            case Image: {
                Image fxImage = ImageHelper.readImage(toResourceDir(element.getName()));
                BackgroundImageObject backgroundImageObject = new BackgroundImageObject(fxImage, element.getName());
                backgroundImageObject.getImageView().setX(element.getLocation().getX());
                backgroundImageObject.getImageView().setY(element.getLocation().getY());
                result = backgroundImageObject;

                break;
            }
            case Rectangle: {
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
                result = new BackgroundShapeObject(element.getName(), rectangle);
                break;
            }
            default:
                throw new IllegalArgumentException(String.format("Unknown element type '%s'.", element.getType()));
        }
        return result;
    }

}
