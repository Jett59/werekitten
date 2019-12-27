package com.mycodefu.werekitten;

import java.io.IOException;

import com.mycodefu.werekitten.application.Application;
import com.mycodefu.werekitten.sound.MusicPlayer;

public class Start{
    public static void main(String[] args) throws IOException {
    	MusicPlayer.setInLevel();
    	Application.launch(args);
    }
}
