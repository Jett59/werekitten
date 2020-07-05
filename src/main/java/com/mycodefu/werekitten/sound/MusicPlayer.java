package com.mycodefu.werekitten.sound;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class MusicPlayer {
    private static ClipData levelTheme = gameThemeInit("Level_Theme");

    public static void play() {
    	System.out.println("playing");
        levelTheme.clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public static void stop() {
        levelTheme.clip.stop();
    }

    public static void shutdown() {
    	System.out.println("closing level!");
    	levelTheme.close();
    }
    
    private static ClipData gameThemeInit(String themeName) {
        String resourcePath = "/music/" + themeName + ".wav";
        return getClipFromResource(resourcePath);

    }

    public static ClipData getClipFromResource(String resourcePath) {
        try {
            InputStream in = MusicPlayer.class.getResourceAsStream(resourcePath);
            BufferedInputStream bufIn = new BufferedInputStream(in);
            AudioInputStream audIn = AudioSystem.getAudioInputStream(bufIn);
            Clip c = AudioSystem.getClip();
            c.open(audIn);
            return new ClipData(c, in, bufIn, audIn);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static class ClipData {
    	private Clip clip;
    	private InputStream in;
    	private BufferedInputStream bufIn;
    	private AudioInputStream audIn;
    
    private ClipData(Clip clip, InputStream in, BufferedInputStream bufIn, AudioInputStream audIn) {
    	this.audIn = audIn;
    	this.bufIn = bufIn;
    	this.clip = clip;
    	this.in = in;
    }
    
    private void close() {
    	try {
			in.close();
    	bufIn.close();
    	audIn.close();
    	clip.close();
    	} catch (Exception e) {
			e.printStackTrace();
		}
    }
    }
}
