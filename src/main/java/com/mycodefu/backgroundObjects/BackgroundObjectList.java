package com.mycodefu.backgroundObjects;

import java.util.*;

public class BackgroundObjectList {
	private List<NodeObject> backgroundObjects = new ArrayList<>();
	public boolean add(NodeObject obj2) {
		for(NodeObject obj : backgroundObjects) {
		if(obj2.getName() == obj.getName()) {
			throw new IllegalArgumentException("an object with the same name already exists, names must be unique, name: "+obj.getName());
		}
		}
		return backgroundObjects.add(obj2);
	}
	public NodeObject getBackgroundObjectByName(String name) {
		for(NodeObject obj : backgroundObjects) {
			if(obj.getName() == name) {
				return obj;
			}
		}
		throw new IllegalArgumentException("no such object: "+name);
	}
	public void addAllStationary(List<BackgroundObject> list) {
		for(NodeObject obj : list) {
			add(obj);
		}
	}
	public void addAllAnimated(List<AnimatedBackgroundObject> list) {
		for(NodeObject obj : list) {
			add(obj);
		}
	}
	public void addAll(Iterable<NodeObject> list) {
		for(NodeObject obj : list) {
			add(obj);
		}
	}
}
