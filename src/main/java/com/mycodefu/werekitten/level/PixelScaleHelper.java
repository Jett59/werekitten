package com.mycodefu.werekitten.level;

public class PixelScaleHelper {
    private double xMultiplier, yMultiplier;

    public PixelScaleHelper(double scaleWidth, double scaleHeight, double screenWidth, double screenHeight) {
        this.xMultiplier = screenWidth/scaleWidth;
        this.yMultiplier = screenHeight/scaleHeight;
    }

    public double scaleX(double x) {
        return x*xMultiplier;
    }

    public double scaleY(double y) {
        return y*yMultiplier;
    }
    
    public double scaleXBack(double x) {
    	return x/xMultiplier;
    }
    
    public double scaleYBack(double y) {
    	return y/yMultiplier;
    }
}
