package com.mycodefu;

import com.mycodefu.application.Application;
import com.mycodefu.backgroundObjects.BackgroundObjectBuilder;
import com.mycodefu.sound.MusicPlayer;

public class Start {
    public static void main(String[] args) {
    	MusicPlayer.setInMain();
        Application.start(args);
    }
}
