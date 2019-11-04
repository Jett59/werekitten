package com.mycodefu.backgroundObjects;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class BackgroundObjectBuilder {
	private static String toResourceDir(String from) {
		return "/background/freetileset/png/Object/"+from+".png";
	}
public static BackgroundObject build(String name) {
	InputStream in = BackgroundObjectBuilder.class.getResourceAsStream(toResourceDir(name));
	BufferedImage bufferedImage;
	try {
		bufferedImage = ImageIO.read(in);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		throw new RuntimeException(e);
	}
	Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
	BackgroundObject result = new BackgroundObject(fxImage, name);
	return result;
}
public static BackgroundObjectList buildAll() {
	var backgroundObjectList = new BackgroundObjectList();
	for(BackgroundObjectData data : BackgroundObjects.backgroundObjects) {
		var backgroundObjectsInData = IntStream
				.rangeClosed(1, data.count)
				.mapToObj(index->data.name.replaceAll("index", index+""))
				.map(BackgroundObjectBuilder::build)
				.collect(Collectors.toList());
	backgroundObjectList.addAll(backgroundObjectsInData);
    }
	return backgroundObjectList;
}
}
