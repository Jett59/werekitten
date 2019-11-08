package com.mycodefu.backgroundObjects;

import java.util.*;

public class BackgroundObjectList {
	private List<BackgroundObject> backgroundObjects = new ArrayList<>();
	public boolean add(BackgroundObject e) {
		for(BackgroundObject obj : backgroundObjects) {
		if(e.getName() == obj.getName()) {
			throw new IllegalArgumentException("an object with the same name already exists, names must be unique, name: "+obj.getName());
		}
		}
		return backgroundObjects.add(e);
	}
	public BackgroundObject getBackgroundObjectByName(String name) {
		for(BackgroundObject obj : backgroundObjects) {
			if(obj.getName() == name) {
				return obj;
			}
		}
		throw new IllegalArgumentException("no such object: "+name);
	}
	public void addAll(Iterable<BackgroundObject> iterator) {
		for(BackgroundObject obj : iterator) {
			add(obj);
		}
	}
}
