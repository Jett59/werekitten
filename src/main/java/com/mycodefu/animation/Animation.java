package com.mycodefu.animation;

import com.mycodefu.backgroundObjects.AnimatedBackgroundObject;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Animation extends Transition {
    private final ImageView imageView;
    private final int cellCount;
    private final int cellWidth;
    private final int cellHeight;

    private int cellIndex;
    private int lastCellIndex;

    Animation(ImageView imageView, Duration duration, int cellCount, int cellWidth, int cellHeight) {
        this.imageView = imageView;
        this.cellCount = cellCount;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;

        this.setCycleDuration(duration);
        this.setInterpolator(Interpolator.LINEAR);

        this.cellIndex = 0;
        this.lastCellIndex = 0;
        updateViewPort();
    }

    private void updateViewPort() {
        imageView.setViewport(new Rectangle2D(cellIndex * cellWidth, 0, cellWidth, cellHeight));
    }

    public ImageView getImageView() {
        return imageView;
    }

    @Override
    protected void interpolate(double frac) {
        cellIndex = Math.min((int) Math.floor(frac * cellCount), cellCount - 1);
        if (cellIndex != lastCellIndex) {
            updateViewPort();
            lastCellIndex = cellIndex;
        }
    }
    public AnimatedBackgroundObject asBackgroundObject(String name) {
    	return new AnimatedBackgroundObject(this, name);
    }
}
