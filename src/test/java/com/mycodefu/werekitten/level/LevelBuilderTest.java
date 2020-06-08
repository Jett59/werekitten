package com.mycodefu.werekitten.level;

import com.mycodefu.werekitten.animation.AnimationCompiler;
import com.mycodefu.werekitten.backgroundObjects.BackgroundObjectBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelBuilderTest {
    @Test
    void buildLevel() {
        LevelBuilder levelBuilder = new LevelBuilder(new BackgroundObjectBuilder(new AnimationCompiler()));
        GameLevel gameLevel = levelBuilder.buildLevel("/level.wkl", 1024, 768);
        assertNotNull(gameLevel);
    }
}