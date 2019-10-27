package com.mycodefu.animation;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AnimationCompiler {

    public static Animation compileCatAnimation(String name, int count, Duration duration) {
        return compileCatAnimation(name, count, duration, false);
    }
    public static Animation compileCatAnimation(String name, int count, Duration duration, boolean reversed) {
        var images = IntStream
                .rangeClosed(1, count)
                .mapToObj(index -> getCatResourcePath(name, index))
                .map(AnimationCompiler.class::getResourceAsStream)
                .map(resourceStream -> {
                    try {
                        return ImageIO.read(resourceStream);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to read the image.");
                    }
                })
                .collect(Collectors.toList());

        int cellWidth = images.get(0).getWidth();
        int cellHeight = images.get(0).getHeight();

        BufferedImage image  = new BufferedImage(cellWidth * count, cellHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();
        for (int i = 0; i < images.size(); i++) {
            BufferedImage img = images.get(i);
            if (reversed) {
                img = flipHorizontal(img);
            }

            graphics.drawImage(img, cellWidth * i, 0, null);
        }
        graphics.dispose();

        Image javaFxImage = SwingFXUtils.toFXImage(image, null);
        ImageView imageView = new ImageView(javaFxImage);

        return new Animation(imageView, duration, count, cellWidth, cellHeight);
    }

    private static BufferedImage flipHorizontal(BufferedImage image) {
        AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(-1, 1));
        at.concatenate(AffineTransform.getTranslateInstance(-image.getWidth(), 0));
        BufferedImage newImage = new BufferedImage(
                image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.transform(at);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }

    private static String getCatResourcePath(String name, int index) {
        final String path_template = "/cat/animations/[namelowercase]/[name] ([index]).png";

        return path_template
                .replace("[namelowercase]", name.toLowerCase())
                .replace("[name]", name)
                .replace("[index]", Integer.toString(index));
    }
}
