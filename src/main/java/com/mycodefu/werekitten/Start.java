package com.mycodefu.werekitten;

import java.io.IOException;

import com.mycodefu.werekitten.animation.AnimationCompiler;
import com.mycodefu.werekitten.application.Application;
import com.mycodefu.werekitten.sound.MusicPlayer;

import javafx.util.Duration;

public class Start {
    public static void main(String[] args) throws IOException {
    	MusicPlayer.setInLevel();
    	AnimationCompiler.compileAnimation("cat", "walk", 10, Duration.seconds(1), false, 10);
        Application.start(args);
    }
}
