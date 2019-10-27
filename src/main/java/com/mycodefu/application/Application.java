package com.mycodefu.application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class Application extends javafx.application.Application{
public static void start(String[] args) {
	Application.launch(args);
}
	@Override
	public void start(Stage stage) throws Exception {
		Group g = new Group();
		Scene s = new Scene(g);
		stage.setScene(s);
		stage.setOnCloseRequest(e->{
			System.exit(0);
		});
		stage.setTitle("werekitten");
		stage.show();
	}

}
