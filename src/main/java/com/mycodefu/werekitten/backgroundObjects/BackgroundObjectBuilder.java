package com.mycodefu.werekitten.backgroundObjects;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.mycodefu.werekitten.image.ImageHelper;

import javafx.scene.image.Image;

public class BackgroundObjectBuilder {
	private static String toResourceDir(String from) {
		return "/background/freetileset/png/Object/"+from+".png";
	}
public static BackgroundObject build(String name) {
	Image fxImage = ImageHelper.readImage(toResourceDir(name));
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
	backgroundObjectList.addAllStationary(backgroundObjectsInData);
    }
	return backgroundObjectList;
}
}
