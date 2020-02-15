package com.mycodefu.werekitten.location;

import com.mycodefu.werekitten.level.PixelScaleHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


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

	@Test
	void scale_back_x_Test() {
		PixelScaleHelper pixelScaleHelper = new PixelScaleHelper(1024, 768, 10240, 7680);
		assertEquals(20, pixelScaleHelper.scaleXBack(200));
	}

	@Test
	void scale_back_y_Test() {
		PixelScaleHelper pixelScaleHelper = new PixelScaleHelper(1024, 768, 10240, 7680);
		assertEquals(70, pixelScaleHelper.scaleYBack(700));
	}

}
