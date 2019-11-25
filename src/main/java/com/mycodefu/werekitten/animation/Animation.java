package com.mycodefu.werekitten.animation;

import com.mycodefu.werekitten.backgroundObjects.BackgroundAnimationObject;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * A transition based animation which moves a viewport over a filmstrip style image.
 *
 * The image contains a number of cells horizontally.
 *
 * The image should be the same height as the cellHeight.
 *
 * The image width should be the cellCount * the cellWidth.
 *
 * The transition between cells will be spaced across the duration time period.
 */
public class Animation extends Transition {
    private final ImageView imageView;
    private final int cellCount;
    private final int cellWidth;
    private final int cellHeight;

    private int cellIndex;
    private int lastCellIndex;

    Animation(ImageView imageView, Duration duration, int cellCount, int cellWidth, int cellHeight) {
        checkImageView(imageView, cellCount, cellWidth, cellHeight);

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

    private void checkImageView(ImageView imageView, int cellCount, int cellWidth, int cellHeight) {
        if (imageView == null) {
            throw new RuntimeException("Null image passed as the filmstrip image for an animation.");
        } else if (imageView.getImage().getHeight() != cellHeight) {
            throw new RuntimeException(String.format("Image passed as the filmstrip image for an animation is the incorrect height for the cells. Expected %d, was %f.", cellHeight, imageView.getImage().getHeight()));
        } else if (imageView.getImage().getWidth() != cellWidth * cellCount) {
            throw new RuntimeException(String.format("Image passed as the filmstrip image for an animation is the incorrect width for the cells. Expected %d, was %f.", cellWidth * cellCount, imageView.getImage().getWidth()));
        }
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
    public BackgroundAnimationObject asBackgroundObject(String name) {
    	return new BackgroundAnimationObject(this, name);
    }
}
