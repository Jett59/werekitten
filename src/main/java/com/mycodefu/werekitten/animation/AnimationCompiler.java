package com.mycodefu.werekitten.animation;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.mycodefu.werekitten.builder.ImageToPolygon;
import com.mycodefu.werekitten.image.ImageHelper;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

/**
 * Creates an animation from a resource folder full of animation cell images.
 * <p>
 * Resource location pattern:
 * /characters/[character]/animations/[animationlowercase]/[animation] ([index]).png
 * e.g. /characters/cat/animations/walk/Walk (1).png
 */
public class AnimationCompiler {

    public Animation compileAnimation(String character, String animation, int count, Duration duration) {
        return compileAnimation(character, animation, count, duration, false);
    }

    public Animation compileAnimation(String character, String animation, int count, Duration duration, boolean reversed) {
        return compileAnimation(character, animation, count, duration, reverseImages(reversed));
    }

    private static ImageProcess reverseImages(boolean reverse) {
    	if(reverse) {
    	return images->images.stream().map(ImageHelper::reverseImage).collect(Collectors.toList());
    	}else {
    		return images->images;
    	}
    }
    
    private static ImageProcess scaleImages(double scale) {
    	return images->images.stream().map(img->ImageHelper.scaleImage(img, scale)).collect(Collectors.toList());
    }
    
    private static ImageProcess scaleImagesToHeight(double height) {
    	return images->{
    			BufferedImage firstImage = images.get(0);
    			int firstImageHeight = firstImage.getHeight();
    			double scale = (double)height/(double)firstImageHeight;
    			return scaleImages(scale).process(images);
    	};
    }
    
    private static ImageProcess scaleImagesToWidth(double width) {
    	return images->{
			BufferedImage firstImage = images.get(0);
			int firstImageWidth = firstImage.getWidth();
			double scale = (double)width/(double)firstImageWidth;
			return scaleImages(scale).process(images);
	};
    }
    
    private static ImageProcess scaleImagesToWidthOrHeight(String widthOrHeight, double size) {
    	return widthOrHeight=="width" ? scaleImagesToWidth(size) : widthOrHeight == "height" ? scaleImagesToHeight(size) : null;
    }
    
    public Animation compileAnimation(String character, String animation, int count, Duration duration, boolean reversed, double scale) {
    	return compileAnimation(character, animation, count, duration, scaleImages(scale), reverseImages(reversed));
    }

    public Animation compileAnimation(String character, String animation, int count, Duration duration, boolean reversed, double size, String widthOrHeight) {
    	return compileAnimation(character, animation, count, duration, reverseImages(reversed), scaleImagesToWidthOrHeight(widthOrHeight, size));
    }
    
	public Animation compileAnimation(String character, String animation, int count, Duration duration, ImageProcess... imageProcesses) {
        List<BufferedImage> images = IntStream
                .rangeClosed(1, count)
                .mapToObj(index -> getResourcePath(character, animation, index))
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
