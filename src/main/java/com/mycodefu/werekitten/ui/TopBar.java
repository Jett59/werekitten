package com.mycodefu.werekitten.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class TopBar extends javafx.scene.layout.VBox {
    public static final String WERE_KITTEN_TITLE = "Welcome to WereKitten! Listing on port: %d, Kitten x: %.0f";
    private final TextField connectionAddress;
    private final Button connectionButton;
    private final FlowPane connectionControls;
    private final FlowPane flowLayout;
    Text title;
    private int port;
    private double x_value;

    public TopBar(Pane parent, UIConnectCallback connectCallback){
        super();
        this.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        this.setOpacity(0.25);
        this.setMinHeight(25);
        this.setFillWidth(true);
        this.prefWidthProperty().bind(parent.widthProperty());

        this.flowLayout = new javafx.scene.layout.FlowPane();

        this.port = 0;
        this.x_value = 0;

        title = new Text();
        title.setFill(Color.BLACK);
        this.flowLayout.getChildren().add(title);

        this.connectionAddress = new javafx.scene.control.TextField("ws://127.0.0.1:50000");
        this.connectionButton = new javafx.scene.control.Button("Connect");
        this.connectionButton.setOnAction(actionEvent -> {
            if (this.connectionButton.getText().equalsIgnoreCase("Connect")) {
                connectCallback.connect(connectionAddress.getText());
            } else {
                connectCallback.disconnect();
            }
        });
        this.connectionControls = new FlowPane();
        this.connectionControls.getChildren().add(connectionAddress);
        this.connectionControls.getChildren().add(connectionButton);

        this.flowLayout.getChildren().add(connectionControls);

        this.getChildren().add(flowLayout);

        updateTitle();

        this.setAlignment(Pos.CENTER_LEFT);
    }

    private void updateTitle() {
        title.setText(String.format(WERE_KITTEN_TITLE, port, x_value));
    }

    public void updateXAmount(double value) {
        this.x_value = value;
        updateTitle();
    }

    void updateConnectionState(boolean connected){
        this.connectionControls.setDisable(connected);
        if (connected) {
            this.connectionButton.setText("Disconnect");
        } else {
            this.connectionButton.setText("Connect");
        }
    }

    public void setPort(int port) {
        this.port = port;
        updateTitle();
    }
}
