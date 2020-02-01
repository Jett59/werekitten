package com.mycodefu.werekitten.location;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.mycodefu.werekitten.position.PixelScaleHelper;


public class PixelScaleHelperTest {

@Test
void scale_x_Test() {
	PixelScaleHelper pixelScaleHelper = new PixelScaleHelper(200, 200, 1000, 1000);
	assertEquals(100, pixelScaleHelper.scaleX(20));
	assertEquals(500, pixelScaleHelper.scaleX(100));
}

@Test
void scale_y_test() {
	PixelScaleHelper pixelScaleHelper = new PixelScaleHelper(200, 200, 1000, 1000);
	assertEquals(100, pixelScaleHelper.scaleY(20));
	assertEquals(500, pixelScaleHelper.scaleY(100));
}
}
