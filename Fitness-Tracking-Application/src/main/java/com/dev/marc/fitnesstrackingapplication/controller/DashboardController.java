package com.dev.marc.fitnesstrackingapplication.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Arc;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class DashboardController {

	@FXML
	private ProgressBar progressBar;
	@FXML
	private Arc progressArc;
	@FXML
	private Label percentageLabel;

	@FXML
	private Button TrackBtn;
	@FXML
	private Button WaGBtn;
	@FXML
	private Button homeButton;

	@FXML
	private AnchorPane paneContainer;

	private double progress = 0;

	@FXML
	private Label labell;
	@FXML
	private Arc progressArc1;
	@FXML
	private Arc progressArc2;
	@FXML
	private Arc progressArc3;
	@FXML
	private Arc progressArc4;

	@FXML
	private Label label1;
	@FXML
	private Label label2;
	@FXML
	private Label label3;
	@FXML
	private Label label4;

	public void initialize() {
		initializeArcs();

		Timeline timeline = new Timeline(
				new KeyFrame(Duration.seconds(0.05), e -> updateProgress())
		);
		timeline.setCycleCount(100);
		timeline.play();

		applyTypingEffect(labell, "Welcome to Fitness Tracker");
	}

	private void initializeArcs() {
		if (progressArc1 != null) progressArc1.setStartAngle(90);
		if (progressArc2 != null) progressArc2.setStartAngle(90);
		if (progressArc3 != null) progressArc3.setStartAngle(90);
		if (progressArc4 != null) progressArc4.setStartAngle(90);
	}

	private void updateProgress() {
		if (progress < 100) {
			progress += 1;
			progressBar.setProgress(progress / 100);
			updateArc(progressArc1, label1);
			updateArc(progressArc2, label2);
			updateArc(progressArc3, label3);
			updateArc(progressArc4, label4);
		}
	}

	private void updateArc(Arc arc, Label label) {
		if (arc != null) {
			arc.setLength(-progress * 3.6);
		}
		if (label != null) {
			label.setText((int) progress + "%");
		}
	}

	@FXML
	public void goToProfile(ActionEvent event) throws IOException {
		loadScene("/path/to/Profile.fxml", event);
	}

	@FXML
	public void goToTrack(ActionEvent event) throws IOException {
		loadScene("/path/to/Map.fxml", event);
	}

	@FXML
	public void goToWorkoutGoals(ActionEvent event) throws IOException {
		loadScene("/path/to/WorkoutGoals.fxml", event);
	}

	@FXML
	public void goToNutrition(ActionEvent event) throws IOException {
		loadScene("/path/to/Nutrition.fxml", event);
	}

	@FXML
	public void toRegister(ActionEvent event) throws IOException {
		loadScene("/path/to/Register.fxml", event);
	}

	private void loadScene(String fxmlPath, ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
		Parent root = loader.load();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(new Scene(root));
		stage.show();
	}

	public static void applyTypingEffect(Label labell, String text) {
		final StringBuilder displayedText = new StringBuilder();
		Timeline timeline = new Timeline(
				new KeyFrame(Duration.millis(100), event -> {
					if (displayedText.length() < text.length()) {
						displayedText.append(text.charAt(displayedText.length()));
						labell.setText(displayedText.toString());
					}
				})
		);
		timeline.setCycleCount(text.length());
		timeline.play();

	}

}
