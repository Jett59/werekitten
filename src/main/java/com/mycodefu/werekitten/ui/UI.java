package com.mycodefu.werekitten.ui;

import com.mycodefu.werekitten.player.Player;

public interface UI {
Player getPlayer1();

Player getPlayer2();

    void addUIEventListener(UIEventCallback callback);

    void updateConnectionState(boolean connected);
}
