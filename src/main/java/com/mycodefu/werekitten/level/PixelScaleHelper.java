package com.mycodefu.werekitten.level;

public class PixelScaleHelper {
    private double scaleWidth;
    private double scaleHeight;
    private double screenWidth;
    private double screenHeight;

    public PixelScaleHelper(double scaleWidth, double scaleHeight, double screenWidth, double screenHeight) {
        this.scaleHeight = scaleHeight;
        this.scaleWidth = scaleWidth;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

    public double scaleX(double x) {
        double ratio = x / scaleWidth;
        double result = ratio * screenWidth;
        return result;
    }

    public double scaleY(double y) {
        double ratio = y / scaleHeight;
        double result = ratio * screenHeight;
        return result;
    }
    
    public double scaleXBack(double x) {
    	double ratio = x / screenWidth;
    	double result = ratio * scaleWidth;
    	return result;
    }
    
    public double scaleYBack(double y) {
    	double ratio = y / screenHeight;
    	double result = ratio * scaleHeight;
    	return result;
    }
}
