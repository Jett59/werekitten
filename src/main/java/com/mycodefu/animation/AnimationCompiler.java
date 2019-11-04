package com.mycodefu.animation;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.awt.Image.SCALE_SMOOTH;

public class AnimationCompiler {

    public static Animation compileCatAnimation(String name, int count, Duration duration) {
        return compileCatAnimation(name, count, duration, false);
    }

    public static Animation compileCatAnimation(String name, int count, Duration duration, boolean reversed) {
        return compileCatAnimation(name, count, duration, reversed, 1.0d);
    }

    public static Animation compileCatAnimation(String name, int  count, Duration duration, boolean reversed, double scale) {
        List<Image> images = IntStream
                .rangeClosed(1, count)
                .mapToObj(index -> getCatResourcePath(name, index))
                .map(AnimationCompiler.class::getResourceAsStream)
                .map(AnimationCompiler::readImage)
                .map(image -> reversed ? reverseImage(image) : image)
                .map(image -> scale != 1.0d ? scaleImage(image, scale) : image)
                .collect(Collectors.toList());

        int cellWidth = images.get(0).getWidth(null);
        int cellHeight = images.get(0).getHeight(null);

        BufferedImage animationStrip = createAnimationStrip(images, cellWidth, cellHeight);

        ImageView imageView = new ImageView(SwingFXUtils.toFXImage(animationStrip, null));

        return new Animation(imageView, duration, count, cellWidth, cellHeight);
    }

    private static String getCatResourcePath(String name, int index) {
        final String path_template = "/cat/animations/[namelowercase]/[name] ([index]).png";

        return path_template
                .replace("[namelowercase]", name.toLowerCase())
                .replace("[name]", name)
                .replace("[index]", Integer.toString(index));
    }

    private static BufferedImage readImage(InputStream resourceStream) {
        try {
            return ImageIO.read(resourceStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the image.");
        }
    }

    private static Image scaleImage(Image image, double scale) {
        int newWidth = (int) ((double) image.getWidth(null) * scale);
        int newHeight = (int) ((double) image.getHeight(null) * scale);
        return image.getScaledInstance(newWidth, newHeight, SCALE_SMOOTH);
    }

    private static Image reverseImage(Image image) {
        AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(-1, 1));
        at.concatenate(AffineTransform.getTranslateInstance(-image.getWidth(null), 0));
        BufferedImage newImage = new BufferedImage(
                image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.transform(at);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }

    private static BufferedImage createAnimationStrip(List<Image> images, int cellWidth, int cellHeight) {
        BufferedImage image = new BufferedImage(cellWidth * images.size(), cellHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();
        for (int i = 0; i < images.size(); i++) {
            graphics.drawImage(images.get(i), cellWidth * i, 0, null);
        }
        graphics.dispose();
        return image;
    }
}
