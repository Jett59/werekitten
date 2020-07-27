package com.mycodefu.werekitten.level.data;

public class Element {
    private ElementType type;
    private AnimationConfig animationConfig;
    private String name;
    private Size size;
    private Location location;
    private Color fillColor;
    private Color strokeColor;
    private double strokeWidth;
    private String path;

    public Element() {

    }

    public ElementType getType() {
        return type;
    }

    public void setType(ElementType type) {
        this.type = type;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public Color getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
    }

    public double getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(double strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

	public AnimationConfig getAnimationConfig() {
		return animationConfig;
	}

	public void setAnimationConfig(AnimationConfig animationConfig) {
		this.animationConfig = animationConfig;
	}

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
