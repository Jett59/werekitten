package com.mycodefu.werekitten.ui.settings;

import com.mycodefu.werekitten.pipeline.PipelineContext;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class SettingsPresenter implements Initializable {

    @FXML
    BorderPane pane;

    @FXML
    VBox settings;

    @Inject
    PipelineContext context;

    @Inject
    Text welcome;

    @Inject
    FlowPane imagePane;

    HBox[] preferences;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pane.setTop(welcome);
        pane.setLeft(imagePane);

        preferences = new HBox[context.getPreferences().keySet().size()];
        AtomicInteger index = new AtomicInteger();
        context.getPreferences().forEach((key, value) -> {
            Text text = new Text(key);
            text.setFocusTraversable(true);
            preferences[index.get()] = new HBox(text, new TextField(value));
            index.addAndGet(1);
        });

        settings.getChildren().add(new VBox(preferences));
    }

    @FXML
    public void cancel() {
        closeWindow();
    }

    @FXML
    public void save() {
        for (HBox box : preferences) {
            String key = ((Text) (box.getChildren().get(0))).getText();
            String value = ((TextField) (box.getChildren().get(1))).getText();
            context.getPreferences().put(key, value);
        }
        closeWindow();
    }

    private void closeWindow() {
        Stage window = (Stage)pane.getScene().getWindow();
        window.close();
    }
}
