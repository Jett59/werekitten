package com.mycodefu.animation;

import javafx.embed.swing.SwingFXUtils;
import javafx.util.Duration;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class AnimationCompilerTest {

    @Test
    void compileAnimation() throws IOException {
        Animation animation = AnimationCompiler.compileAnimation("shrew", "Idle", 2, Duration.millis(500));
        assertNotNull(animation);

        File outputAnimationFile = new File("test-shrew.png");
        ImageIO.write(SwingFXUtils.fromFXImage(animation.getImageView().getImage(), null), "PNG", outputAnimationFile);

        assertEquals(1084d, animation.getImageView().getImage().getWidth());
        assertEquals(368d, animation.getImageView().getImage().getHeight());

        assertTrue(binaryCompareStreams(AnimationCompilerTest.class.getResourceAsStream("/test-shrew.png"), new FileInputStream(outputAnimationFile)));
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
            throw new RuntimeException("error reading from stream");
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