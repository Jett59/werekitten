package com.mycodefu.image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class ImageHelper {
	public static Image readImage(String resourceFilePath) {
		InputStream in = ImageHelper.class.getResourceAsStream(resourceFilePath);
			BufferedImage img;
			try {
				img = ImageIO.read(in);
			} catch (IOException e) {
				throw new IllegalArgumentException("could not load the image: "+resourceFilePath+", original exception: "+e.getMessage());
			}
			return SwingFXUtils.toFXImage(img, null);
	}
}