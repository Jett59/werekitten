package com.mycodefu.werekitten.pipeline.handlers.keyboard;

import com.mycodefu.werekitten.pipeline.PipelineContext;
import com.mycodefu.werekitten.pipeline.PipelineEvent;
import com.mycodefu.werekitten.pipeline.handlers.PipelineHandler;
import com.mycodefu.werekitten.player.Kitten;

public class PlayerKeyboardHandler implements PipelineHandler{

	@Override
	public void handleEvent(PipelineContext context, PipelineEvent event) {
		if(event.getPipelineName().equalsIgnoreCase("keyboard")) {
			switch (event.getEvent().getName()) {
			case "leftPressed": {
				context.getPlayerMap().get("local").moveLeft(Kitten.MOVE_AMOUNT);
				break;
			}
			case "rightPressed": {
				context.getPlayerMap().get("local").moveRight(Kitten.MOVE_AMOUNT);
				break;
			}
			case "leftReleased": {
				context.getPlayerMap().get("local").stopMovingLeft();
				break;
			}
			case "rightReleased": {
				context.getPlayerMap().get("local").stopMovingRight();
				break;
			}
			case "spacePressed": {
				context.getPlayerMap().get("local").jump();
			}
			}
		}
		}

}
