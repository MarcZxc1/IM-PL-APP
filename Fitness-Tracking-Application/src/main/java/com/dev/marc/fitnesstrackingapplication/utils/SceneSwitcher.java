package com.dev.marc.fitnesstrackingapplication.utils;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneSwitcher  extends  Transtion{
	public static void switchScene(Stage stage, String fxmlPath,
								   double width, double height,
								   boolean withAnimation) throws IOException {
		Parent newRoot = new SceneSwitcher().loadFXML(fxmlPath);
		if (newRoot == null) {
			System.err.println("Failed to load FXML file: " + fxmlPath);
			return;
		}

		StackPane transitionRoot = new StackPane();
		transitionRoot.getChildren().add(newRoot);

		Rectangle overlay = null;
		if (withAnimation) {
			overlay = new SceneSwitcher().createOverlay(width, height);
			overlay.setOpacity(0);
			transitionRoot.getChildren().add(overlay);
		}

		Scene newScene = new Scene(transitionRoot, width, height);
		if (withAnimation) {
			new SceneSwitcher().applyAnimation(transitionRoot, newRoot, overlay);
		} else {
			stage.setScene(newScene);
		}

		stage.setWidth(width);
		stage.setHeight(height);
		stage.centerOnScreen();
	}
}
