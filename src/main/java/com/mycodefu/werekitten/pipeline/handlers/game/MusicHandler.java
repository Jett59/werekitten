package com.mycodefu.werekitten.pipeline.handlers.game;

import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.sound.MusicPlayer;

public class MusicHandler implements PipelineHandler{

	@Override
	public void handleEvent(PipelineContext context, PipelineEvent event) {
		if(!event.getPipelineName().equalsIgnoreCase("game")) {
			throw new IllegalArgumentException("the music handler is not allowed to be in a pipeline other than game, pipeline: "+event.getPipelineName());
		}else {
			switch (event.getEvent().getName()) {
			case "start": {
				MusicPlayer.setInLevel();
				MusicPlayer.stopPlayingLevel();
				break;
			}
			
			case "levelLoaded": {
				MusicPlayer.playLevel();
				break;
			}
			
			case "quit": {
				MusicPlayer.stopPlayingLevel();
				break;
			}
			
			default:
				break;
			}
		}
	}

}
