package com.mycodefu.werekitten;

import com.mycodefu.werekitten.application.Application;
import com.mycodefu.werekitten.level.LevelReader;
import com.mycodefu.werekitten.sound.MusicPlayer;

public class Start {
    public static void main(String[] args) {
    	MusicPlayer.setInLevel();
        Application.start(args);
    }
}
