package com.mycodefu.werekitten.sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class MusicPlayer {
    private static Clip levelTheme = gameThemeInit("Level_Theme");
    private static boolean shouldPlayLevel = false;

    public static void playLevel() {
    	shouldPlayLevel = true;
        new Thread(() -> {
            while (shouldPlayLevel) {
                if (levelTheme.getFramePosition() == 0) {
                    levelTheme.start();
                } else if (levelTheme.getFramePosition() >= levelTheme.getFrameLength()) {
                    levelTheme.setFramePosition(0);
                }
            }
        }, "level theme thread").start();
        levelTheme.stop();
    }

    public static void stopPlayingLevel() {
        shouldPlayLevel = false;
    }

    private static Clip gameThemeInit(String themeName) {
        try {
            InputStream in = new MusicPlayer().getClass().getResourceAsStream("/music/" + themeName + ".wav");
            BufferedInputStream bufIn = new BufferedInputStream(in);
            AudioInputStream audIn = AudioSystem.getAudioInputStream(bufIn);
            Clip c = AudioSystem.getClip();
            c.open(audIn);
            return c;
        } catch (Exception e) {
            return null;
        }
    }
}
