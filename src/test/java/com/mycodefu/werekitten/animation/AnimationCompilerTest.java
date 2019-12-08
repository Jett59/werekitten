package com.mycodefu.werekitten.animation;

import javafx.embed.swing.SwingFXUtils;
import javafx.util.Duration;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class AnimationCompilerTest {

    @Test
    public void compileAnimation() throws IOException {
        Animation animation = AnimationCompiler.compileAnimation("shrew", "Idle", 2, Duration.millis(500));
        assertNotNull(animation);

        checkSize(animation, 1084d, 368d);

        testCompiledImage(animation, "test-shrew.png");
    }

    @Test
    public void compileAnimation_reversed() throws IOException {
        Animation animation = AnimationCompiler.compileAnimation("shrew", "Idle", 2, Duration.millis(500), true);
        assertNotNull(animation);

        checkSize(animation, 1084d, 368d);

        testCompiledImage(animation, "test-shrew-reversed.png");
    }

    @Test
    public void compileAnimation_scaled() throws IOException {
        Animation animation = AnimationCompiler.compileAnimation("shrew", "Idle", 2, Duration.millis(500), false, 0.5d);
        assertNotNull(animation);

        checkSize(animation, 542d, 184d);

        testCompiledImage(animation, "test-shrew-halfscale.png");
    }

    @Test
    public void compileAnimation_scaled_reversed() throws IOException {
        Animation animation = AnimationCompiler.compileAnimation("shrew", "Idle", 2, Duration.millis(500), true, 0.5d);
        assertNotNull(animation);

        checkSize(animation, 542d, 184d);

        testCompiledImage(animation, "test-shrew-halfscale-reversed.png");
    }

    @Test
    public void compileAnimation_resized() throws IOException{
    Animation animation = AnimationCompiler.compileAnimation("shrew", "idle", 2, Duration.millis(500), false, 237, "height");
    assertNotNull(animation);
    
    checkSize(animation, 698d, 237d);
    
    testCompiledImage(animation, "test-shrew-resized-237.png");
    }
    
@Test
public void compileAnimation_resized_reversed() throws IOException{
        Animation animation = AnimationCompiler.compileAnimation("shrew", "idle", 2, Duration.millis(500), true, 237);
        assertNotNull(animation);
        
        checkSize(animation, 698, 237);
        
        testCompiledImage(animation, "test-shrew-resized-237-reversed.png");
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