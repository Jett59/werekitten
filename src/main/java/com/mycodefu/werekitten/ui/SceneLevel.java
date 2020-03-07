package com.mycodefu.werekitten.ui;

import com.mycodefu.werekitten.level.GameLevel;
import javafx.scene.Scene;

public class SceneLevel {
    private final Scene scene;
    private final GameLevel level;

    public SceneLevel(Scene scene, GameLevel level) {
        this.scene = scene;
        this.level = level;
    }

    public Scene getScene() {
        return scene;
    }

    public GameLevel getLevel() {
        return level;
    }
}
