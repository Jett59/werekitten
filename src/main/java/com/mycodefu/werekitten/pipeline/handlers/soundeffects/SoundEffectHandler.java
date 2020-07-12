package com.mycodefu.werekitten.pipeline.handlers.soundeffects;

import com.mycodefu.werekitten.Start;
import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.GameEventType;
import com.mycodefu.werekitten.event.SoundEffectEventType;
import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.events.game.QuitGameEvent;
import com.mycodefu.werekitten.pipeline.events.soundeffects.PlaySoundEffectEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.sound.MusicPlayer;
import com.mycodefu.werekitten.sound.MusicPlayer.ClipData;

import java.util.HashMap;

import static com.mycodefu.werekitten.sound.MusicPlayer.getClipFromResource;

public class SoundEffectHandler  implements PipelineHandler {
    HashMap<String, MusicPlayer.ClipData> soundEffects = new HashMap<>();

    @Override
    public Event[] getEventInterest() {
        return new Event[]{
                SoundEffectEventType.play,
                GameEventType.quit
        };
    }

    @Override
    public void handleEvent(PipelineContext context, PipelineEvent event) {
        if (event instanceof PlaySoundEffectEvent) {
            PlaySoundEffectEvent playSoundEffectEvent = (PlaySoundEffectEvent)event;
            if (Start.DEBUG_PIPELINE_EVENTS) {
                System.out.printf("Playing sound effect: %s", playSoundEffectEvent);
            }
            String soundEffect = playSoundEffectEvent.getSoundEffect();
            try {
                if (!soundEffects.containsKey(soundEffect)){
                    MusicPlayer.ClipData clipData = getClipFromResource(String.format("/soundeffects/%s.wav", soundEffect));
                    soundEffects.put(soundEffect, clipData);
                }

                ClipData clipData = soundEffects.get(soundEffect);
                clipData.getClip().start();

            } catch (Exception e) {
                System.out.println("Failed to play sound effect: " + soundEffect);
                e.printStackTrace();
            }
        } else if (event instanceof QuitGameEvent){
            for (ClipData clipData : soundEffects.values()) {
                clipData.close();
            }
        }
    }

}
