package com.mycodefu.werekitten.animation;

import com.mycodefu.werekitten.builder.ImageToPolygon;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

import com.mycodefu.werekitten.image.ImageHelper;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.awt.Image.SCALE_SMOOTH;

/**
 * Creates an animation from a resource folder full of animation cell images.
 * <p>
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

    public static Animation compileAnimation(String character, String animation, int count, Duration duration, boolean reversed, double scale) {
    	return compileAnimation(character, animation, count, duration, new ImageProcess() {

			@Override
			public List<BufferedImage> process(List<BufferedImage> images) {
				if(scale >= 0d) {
				var newImages = new ArrayList<BufferedImage>();
				for(BufferedImage img : images) {
					newImages.add(scaleImage(img, scale));
				}
				return newImages;
				}else {
					throw new IllegalArgumentException("scale parameter outside of valid range: "+scale);
				}
			}
    	}, new ImageProcess() {

			@Override
			public List<BufferedImage> process(List<BufferedImage> images) {
				if(reversed) {
					var newImages = new ArrayList<BufferedImage>();
					for(BufferedImage img : images) {
						newImages.add(reverseImage(img));
					}
					return newImages;
				}else {
				return images;
				}
			}
    		
    	});
    }
    
    public static Animation compileAnimation(String character, String animation, int count, Duration duration, ImageProcess... imageProcesses) {
        List<BufferedImage> images = IntStream
                .rangeClosed(1, count)
                .mapToObj(index -> getResourcePath(character, animation, index))
                .map(log -> {
                    System.out.println(log);
                    return log;
                })
                .map(ImageHelper::readBufferedImage)
                .collect(Collectors.toList());

        if(imageProcesses != null && imageProcesses.length > 0) {
        	for(ImageProcess process : imageProcesses) {
        		images = process.process(images);
        	}
        }
        
         int cellWidth = images.get(0).getWidth(null);
        int cellHeight = images.get(0).getHeight(null);

        BufferedImage animationStrip = createAnimationStrip(images, cellWidth, cellHeight);

        ImageView imageView = new ImageView(SwingFXUtils.toFXImage(animationStrip, null));

        List<Polygon> cellPolygons = images.stream().map(bufferedImage -> ImageToPolygon.getPolygon(bufferedImage, 32)).collect(Collectors.toList());

        return new Animation(imageView, duration, count, cellWidth, cellHeight, cellPolygons);
    }

    private static String getResourcePath(String character, String animation, int index) {
        final String path_template = "/characters/[character]/animations/[animationlowercase]/[animation] ([index]).png";

        return path_template
                .replace("[character]", character)
                .replace("[animationlowercase]", animation.toLowerCase())
                .replace("[animation]", animation)
                .replace("[index]", Integer.toString(index));
    }

    private static BufferedImage scaleImage(Image image, int newWidth, int newHeight) {
        return toBufferedImage(image.getScaledInstance(newWidth, newHeight, SCALE_SMOOTH));
    }

    public static BufferedImage toBufferedImage(Image img) {
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

    private static BufferedImage scaleImage(Image image, double scale) {
        int newWidth = (int) ((double) image.getWidth(null) * scale);
        int newHeight = (int) ((double) image.getHeight(null) * scale);
        return scaleImage(image, newWidth, newHeight);
    }

    private static BufferedImage reverseImage(Image image) {
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

    private static BufferedImage createAnimationStrip(List<BufferedImage> images, int cellWidth, int cellHeight) {
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
