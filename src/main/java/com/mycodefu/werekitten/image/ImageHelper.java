package com.mycodefu.werekitten.image;

import static java.awt.Image.SCALE_SMOOTH;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class ImageHelper {
    public static BufferedImage readBufferedImage(String resourceFilePath) {
        InputStream in = ImageHelper.class.getResourceAsStream(resourceFilePath);
        if (in == null) {
            String message = String.format("Could not load the image: %s, getResourceAsStream returned null.", resourceFilePath);
            System.out.println(message);
            throw new IllegalArgumentException(message);
        }
        BufferedImage img;
        try {
            img = ImageIO.read(in);
        } catch (IOException e) {
            throw new IllegalArgumentException("could not load the image: " + resourceFilePath + ", original exception: " + e.getMessage());
        }
        return img;
    }
    public static BufferedImage safeReadImageFile(File file) {
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            throw new IllegalArgumentException("could not load the image: " + file.getAbsolutePath() + ", original exception: " + e.getMessage());
        }
    }

    public static Image readFxImage(String resourcePath) {
        return SwingFXUtils.toFXImage(readBufferedImage(resourcePath), null);
    }
    
    public static BufferedImage scaleImage(java.awt.Image image, int newWidth, int newHeight) {
        return toBufferedImage(image.getScaledInstance(newWidth, newHeight, SCALE_SMOOTH));
    }
 
    public static BufferedImage scaleImage(java.awt.Image image, double scale) {
        int newWidth = (int) ((double) image.getWidth(null) * scale);
        int newHeight = (int) ((double) image.getHeight(null) * scale);
        return scaleImage(image, newWidth, newHeight);
    }
    
    public static BufferedImage toBufferedImage(java.awt.Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
 
    public static BufferedImage reverseImage(java.awt.Image image) {
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

    
}