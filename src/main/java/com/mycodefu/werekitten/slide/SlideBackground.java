package com.mycodefu.werekitten.slide;

import java.util.ArrayList;
import java.util.List;

import com.mycodefu.werekitten.backgroundObjects.NodeObject;

import javafx.scene.Group;
import javafx.scene.Node;

import java.util.stream.*;

public class SlideBackground {
private final ArrayList<NodeObject> nodes = new ArrayList<>();
private Group g = new Group();
private SlideBackground(){}
private Stream<NodeObject> getBackgroundObjectsFromNodes() {
	return IntStream
			.range(0, nodes.size())
			.mapToObj(nodes::get);
}
public List<Node> getNodes(){
	return 
			getBackgroundObjectsFromNodes()
			.map(object->object.getNode())
			.collect(Collectors.toList());
}
public void addNode(NodeObject obj) {
	g.getChildren().add(obj.getNode());
	nodes.add(obj);
}
public Group getAsGroup() {
	return g;
}
public void moveX(int moveAmount) {
	g.setTranslateX(g.getTranslateX() + moveAmount);
}

public static SlideBackground empty() {
	return new SlideBackground();
}
public static SlideBackground withNodes(List<NodeObject> nodes) {
	SlideBackground sb = new SlideBackground();
	for(NodeObject no : nodes) {
		sb.addNode(no);
	}
	return sb;
}
public static SlideBackground withNodes(ArrayList<NodeObject> nodes) {
	SlideBackground sB = new SlideBackground();
	for(NodeObject obj : sB.nodes) {
		sB.addNode(obj);
	}
	return sB;
}
}
