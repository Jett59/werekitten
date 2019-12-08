package com.mycodefu.werekitten;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.mycodefu.werekitten.animation.AnimationCompiler;
import com.mycodefu.werekitten.application.Application;
import com.mycodefu.werekitten.sound.MusicPlayer;

import javafx.embed.swing.SwingFXUtils;
import javafx.util.Duration;

public class Start {
    public static void main(String[] args) throws IOException {
    	ImageIO.write(SwingFXUtils.fromFXImage(AnimationCompiler.compileAnimation("shrew", "idle", 2, Duration.millis(500), true, 237).getImageView().getImage(), null), "png", new File("test-shrew-resized-237-reversed.png"));
    	MusicPlayer.setInLevel();
    	AnimationCompiler.compileAnimation("cat", "walk", 10, Duration.seconds(1), false, 10);
        Application.start(args);
    }
}
