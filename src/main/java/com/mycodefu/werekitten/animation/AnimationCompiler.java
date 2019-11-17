package com.mycodefu.werekitten.animation;

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

/**
 * Creates an animation from a resource folder full of animation cell images.
 *
 * Resource location pattern:
 * /characters/[character]/animations/[animationlowercase]/[animation] ([index]).png
 * e.g. /characters/cat/animations/walk/Walk (1).png
 */
public class AnimationCompiler {

    public static Animation compileAnimation(String character, String animation, int count, Duration duration) {
        return compileAnimation(character, animation, count, duration, false);
    }

    public static Animation compileAnimation(String character, String animation, int count, Duration duration, boolean reversed) {
        return compileAnimation(character, animation, count, duration, reversed, 1.0d);
    }

    public static Animation compileAnimation(String character, String animation, int  count, Duration duration, boolean reversed, double scale) {
        List<Image> images = IntStream
                .rangeClosed(1, count)
                .mapToObj(index -> getResourcePath(character, animation, index))
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

    private static String getResourcePath(String character, String animation, int index) {
        final String path_template = "/characters/[character]/animations/[animationlowercase]/[animation] ([index]).png";

        return path_template
                .replace("[character]", character)
                .replace("[animationlowercase]", animation.toLowerCase())
                .replace("[animation]", animation)
                .replace("[index]", Integer.toString(index));
    }

    private static BufferedImage readImage(InputStream resourceStream) {
        try {
            return ImageIO.read(resourceStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the image.");
        }
    }
    private static Image scaleImage(Image image, int newWidth, int newHeight) {
        return image.getScaledInstance(newWidth, newHeight, SCALE_SMOOTH);
    }
    private static Image scaleImage(Image image, double scale) {
        int newWidth = (int) ((double) image.getWidth(null) * scale);
        int newHeight = (int) ((double) image.getHeight(null) * scale);
        return scaleImage(image, newWidth, newHeight);
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
            Image cellImage = images.get(i);
            checkWidthHeight(cellImage, cellWidth, cellHeight);

            graphics.drawImage(cellImage, cellWidth * i, 0, null);
        }
        graphics.dispose();
        return image;
    }

    private static void checkWidthHeight(Image cellImage, int cellWidth, int cellHeight) {
        if (cellImage.getWidth(null) != cellWidth) {
            throw new RuntimeException("Unable to load animation cell as the width of a cell does not match the width of the first cell. Ensure that all images in an animation are the same width.");
        } else if (cellImage.getHeight(null) != cellHeight) {
            throw new RuntimeException("Unable to load animation cell as the height of a cell does not match the height of the first cell. Ensure that all images in an animation are the same height.");
        }
    }
}
