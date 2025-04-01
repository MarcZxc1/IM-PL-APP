package com.dev.marc.fitnesstrackingapplication.utils;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class Transtion {
	private static final int FADE_DURATION_MS = 500;

	protected Parent loadFXML(String fxmlPath) throws IOException {
		FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Transtion.class.getResource(fxmlPath)));
		return loader.load();
	}

	protected Rectangle createOverlay(double width, double height) {
		Stop[] stops = new Stop[]{
				new Stop(0, Color.BLACK),
				new Stop(1, Color.TRANSPARENT)
		};
		LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
		return new Rectangle(width, height, gradient);
	}

	protected void applyAnimation(Pane transitionPane, Parent newRoot, Rectangle overlay) {
		if (overlay == null) return;

		// Fade-out transition
		FadeTransition fadeOut = new FadeTransition(Duration.millis(FADE_DURATION_MS), overlay);
		fadeOut.setFromValue(0.0);
		fadeOut.setToValue(1.0);

		// Fade-in transition
		FadeTransition fadeIn = new FadeTransition(Duration.millis(FADE_DURATION_MS), overlay);
		fadeIn.setFromValue(1.0);
		fadeIn.setToValue(0.0);

		// Fade-out completes -> Switch root and start fade-in
		fadeOut.setOnFinished(event -> {
			transitionPane.getChildren().setAll(newRoot); // Switch content
			fadeIn.play(); // Start fade-in
		});

		// Fade-in completes -> Remove overlay
		fadeIn.setOnFinished(event -> transitionPane.getChildren().remove(overlay));

		// Start fade-out
		fadeOut.play();
	}

}