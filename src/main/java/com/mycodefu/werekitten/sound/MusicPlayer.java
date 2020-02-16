package com.mycodefu.werekitten.sound;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class MusicPlayer {
    private static Clip levelTheme = gameThemeInit("Level_Theme");

	public static void playLevel() {
		levelTheme.loop(Clip.LOOP_CONTINUOUSLY);
    }

	public static void stopPlayingLevel() {
		levelTheme.stop();
	}

    private static Clip gameThemeInit(String themeName) {
        try {
            InputStream in = MusicPlayer.class.getResourceAsStream("/music/" + themeName + ".wav");
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
