package com.mycodefu.animation;

import javafx.embed.swing.SwingFXUtils;
import javafx.util.Duration;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.mycodefu.application.Application.SHREW_SCALE;
import static org.junit.jupiter.api.Assertions.*;

class AnimationCompilerTest {

    @Test
    void compileAnimation() throws IOException {
        Animation animation = AnimationCompiler.compileAnimation("shrew", "Idle", 2, Duration.millis(500));
        assertNotNull(animation);

        checkSize(animation, 1084d, 368d);

        testCompiledImage(animation, "test-shrew.png");
    }

    @Test
    void compileAnimation_reversed() throws IOException {
        Animation animation = AnimationCompiler.compileAnimation("shrew", "Idle", 2, Duration.millis(500), true);
        assertNotNull(animation);

        checkSize(animation, 1084d, 368d);

        testCompiledImage(animation, "test-shrew-reversed.png");
    }

    @Test
    void compileAnimation_scaled() throws IOException {
        Animation animation = AnimationCompiler.compileAnimation("shrew", "Idle", 2, Duration.millis(500), false, SHREW_SCALE);
        assertNotNull(animation);

        checkSize(animation, 542d, 184d);

        testCompiledImage(animation, "test-shrew-halfscale.png");
    }

    @Test
    void compileAnimation_scaled_reversed() throws IOException {
        Animation animation = AnimationCompiler.compileAnimation("shrew", "Idle", 2, Duration.millis(500), true, SHREW_SCALE);
        assertNotNull(animation);

        checkSize(animation, 542d, 184d);

        testCompiledImage(animation, "test-shrew-halfscale-reversed.png");
    }


    private void testCompiledImage(Animation animation, String compareImageName) throws IOException {
        File outputAnimationFile = new File(compareImageName);
        ImageIO.write(SwingFXUtils.fromFXImage(animation.getImageView().getImage(), null), "PNG", outputAnimationFile);
        InputStream expectedTestResourceImage = AnimationCompilerTest.class.getResourceAsStream("/animation-compiler-test/" + compareImageName);

        assertTrue(binaryCompareStreams(expectedTestResourceImage, new FileInputStream(outputAnimationFile)), "The animation film-strip image created was not the same as expected.");
    }

    private void checkSize(Animation animation, double width, double height) {
        assertEquals(width, animation.getImageView().getImage().getWidth());
        assertEquals(height, animation.getImageView().getImage().getHeight());
    }

    private boolean binaryCompareStreams(InputStream in1, InputStream in2){
        try {

            int value1,value2;
            do{
                //since we're buffered read() isn't expensive
                value1 = in1.read();
                value2 = in2.read();
                if(value1 !=value2){
                    return false;
                }
            }while(value1 >=0);

            return true;
        } catch (IOException e) {
            System.out.println("Error comparing binary streams");
            e.printStackTrace();
            return false;
        } finally {
            try {
                in1.close();
            } catch (IOException e) {
                throw new RuntimeException("error closing stream");
            }
            try {
                in2.close();
            } catch (IOException e) {
                throw new RuntimeException("error closing stream");
            }
        }
    }
}