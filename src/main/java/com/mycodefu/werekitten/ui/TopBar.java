package com.mycodefu.werekitten.ui;

import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class TopBar extends javafx.scene.layout.VBox {
    public static final String WERE_KITTEN_TITLE = "WereKitten - kitten x: %.0f";
    Text title;

    public TopBar(Pane parent){
        super();
        this.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        this.setOpacity(0.25);
        this.setMinHeight(25);
        this.setFillWidth(true);
        this.prefWidthProperty().bind(parent.widthProperty());

        title = new Text(getTitle(0));
        title.setFill(Color.BLACK);
        this.getChildren().add(title);

        this.setAlignment(Pos.CENTER_LEFT);
    }

    private String getTitle(double x_value) {
        return String.format(WERE_KITTEN_TITLE, x_value);
    }

    public void updateXAmount(double value) {
        title.setText(getTitle(value));
    }

}
