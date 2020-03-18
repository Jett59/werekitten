package com.mycodefu.werekitten.ui;

import com.mycodefu.werekitten.player.Player;

import javafx.scene.Node;

public interface UI {
    void setAddress(String address);

    void addPlayer(Player player);

    void addUIEventListener(UIEventCallback callback);

    void updateConnectionState(boolean connected);

    void removePlayer(Player player);
    
    void addNode(Node n);
}
