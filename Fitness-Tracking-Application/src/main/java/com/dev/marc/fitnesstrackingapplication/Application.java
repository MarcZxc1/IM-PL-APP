package com.dev.marc.fitnesstrackingapplication;

import com.google.api.client.auth.oauth2.Credential;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;


public class Application extends javafx.application.Application {


	private static final String MAIN_VIEW = "/com/dev/marc/fitnesstrackingapplication/view/startUp.fxml";
	private static final String DASHBOARD = "/com/dev/marc/fitnesstrackingapplication/view/Dashboard.fxml";

	@Override
	public void start(Stage stage) {
		try {

			Parent root = FXMLLoader.load(getClass().getResource(DASHBOARD));
			Scene scene = new Scene(root, 1300, 700);
			stage.setTitle("Fitness Tracker");
			Image icon = new Image(getClass().getResourceAsStream("/assets/dumbell.png"));
			stage.getIcons().add(icon);
			stage.setScene(scene);
			stage.show();

		} catch (IOException e) {
			System.err.println("Error occurred: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws Exception {
		CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
		launch();


	}
}
