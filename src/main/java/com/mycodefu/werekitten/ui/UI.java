package com.mycodefu.werekitten.ui;

public interface UI {
    void jump();

    void moveLeft(int amount);

    void moveRight(int amount);

    void stopMovingLeft();

    void stopMovingRight();

    void addUIEventListener(UIEventCallback callback);

    void updateConnectionState(boolean connected);
}
