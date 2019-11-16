package com.mycodefu;

import com.mycodefu.application.Application;
import com.mycodefu.backgroundObjects.BackgroundObjectBuilder;
import com.mycodefu.json.WereKittenLevelAssembler;
import com.mycodefu.sound.MusicPlayer;

public class Start {
    public static void main(String[] args) {
    	WereKittenLevelAssembler.createLevel();
    	MusicPlayer.setInMain();
        Application.start(args);
    }
}
