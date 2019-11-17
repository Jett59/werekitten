package com.mycodefu.werekitten;

import com.mycodefu.werekitten.application.Application;
import com.mycodefu.werekitten.json.WereKittenLevelAssembler;
import com.mycodefu.werekitten.sound.MusicPlayer;

public class Start {
    public static void main(String[] args) {
    	WereKittenLevelAssembler.createLevel();
    	MusicPlayer.setInLevel();
        Application.start(args);
    }
}
