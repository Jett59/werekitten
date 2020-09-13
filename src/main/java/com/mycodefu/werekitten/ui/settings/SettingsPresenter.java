package com.mycodefu.werekitten.ui.settings;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsPresenter implements Initializable {

    @FXML
    BorderPane pane;

    @Inject
    Text welcome;

    @Inject
    FlowPane imagePane;

    public SettingsPresenter() {
        System.out.println("here");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pane.setTop(welcome);
        pane.setLeft(imagePane);
    }
}
