package com.mycodefu.werekitten.pipeline.events.soundeffects;

import com.mycodefu.werekitten.event.Event;
import com.mycodefu.werekitten.event.SoundEffectEventType;
import com.mycodefu.werekitten.pipeline.PipelineEvent;

public class PlaySoundEffectEvent implements PipelineEvent {
    private String soundEffect;

    public PlaySoundEffectEvent(SoundEffects soundEffect){
        this(soundEffect.name());
    }
    public PlaySoundEffectEvent(String soundEffect){
        this.soundEffect = soundEffect;
    }

    @Override
    public String getPipelineName() {
        return "pipeline";
    }

    @Override
    public Event getEvent() {
        return SoundEffectEventType.play;
    }

    public String getSoundEffect() {
        return soundEffect;
    }

    @Override
    public String toString() {
        return soundEffect;
    }
}
