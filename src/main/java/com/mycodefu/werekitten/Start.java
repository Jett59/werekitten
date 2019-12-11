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
    	MusicPlayer.setInLevel();
    	Application.start(args);
    }
}
