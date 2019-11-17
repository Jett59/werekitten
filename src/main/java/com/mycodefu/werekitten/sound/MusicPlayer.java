package com.mycodefu.werekitten.sound;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class MusicPlayer {
private static Clip levelTheme = gameThemeInit("Level_Theme");
private static boolean shouldPlayMain = false;
private static boolean shouldPlayLevel = false;
public static void playLevel() {
	new Thread(()->{
	while(shouldPlayLevel) {
		if(levelTheme.getFramePosition() == 0) {
	levelTheme.start();
		}else if(levelTheme.getFramePosition() >= levelTheme.getFrameLength()){
			levelTheme.setFramePosition(0);
		}
	}
	}, "level theme thread").start();
	levelTheme.stop();
}
private static Clip gameThemeInit(String themeName) {
	try {
		InputStream in = new MusicPlayer().getClass().getResourceAsStream("/music/"+themeName+".wav");
		BufferedInputStream bufIn = new BufferedInputStream(in);
		AudioInputStream audIn = AudioSystem.getAudioInputStream(bufIn);
		Clip c = AudioSystem.getClip();
		c.open(audIn);
		return c;
	}catch(Exception e) {
		return null;
	}
}
public static void setInLevel() {
	shouldPlayLevel = true;
	shouldPlayMain = false;
}
}
