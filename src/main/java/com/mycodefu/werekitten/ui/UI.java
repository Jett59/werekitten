package com.mycodefu.werekitten.ui;

import com.mycodefu.werekitten.player.Player;

public interface UI {
    void setPort(int port);

    void addPlayer(Player player);

    void addUIEventListener(UIEventCallback callback);

    void updateConnectionState(boolean connected);

    void removePlayer(Player player);
}
