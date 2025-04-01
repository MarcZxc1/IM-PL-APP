package com.dev.marc.fitnesstrackingapplication.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Arc;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Controller
public class DashboardController {

	@FXML private ProgressBar progressBar;
	@FXML private Arc progressArc;  // 🔹 Now properly linked
	@FXML private Label percentageLabel;


	@FXML private Button TrackBtn;

	@FXML private Button WaGBtn;

	@FXML private Button homeButton;

	ProfileController profileController = new ProfileController();
	private RestTemplate RestTemplate;
	MapController mapController = new MapController();
	WorkoutAndGoalsController workoutAndGoalsController = new WorkoutAndGoalsController();
	NutritionController nutritionController = new NutritionController();
	RegisterController registerController = new RegisterController();

	@FXML
	AnchorPane paneContainer;

	public void setPaneContainer(AnchorPane paneContainer) {
		this.paneContainer = paneContainer;
	}

	private double progress = 0;


	@FXML private Label labell;

	@FXML private Arc progressArc1;
	@FXML private Arc progressArc2;
	@FXML private Arc progressArc3;
	@FXML private Arc progressArc4;

	// ✅ Labels for each progress arc
	@FXML private Label label1;
	@FXML private Label label2;
	@FXML private Label label3;
	@FXML private Label label4;

	public void initialize() {
		// ✅ Initialize Arcs to start at 90 degrees
		initializeArcs();

		// ✅ Animate progress
		Timeline timeline = new Timeline(
				new KeyFrame(Duration.seconds(0.05), e -> updateProgress())
		);
		timeline.setCycleCount(100); // Run for 100 updates
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

			// ✅ Update the ProgressBar
			progressBar.setProgress(progress / 100);

			// ✅ Update Each Progress Arc Dynamically
			updateArc(progressArc1, label1);
			updateArc(progressArc2, label2);
			updateArc(progressArc3, label3);
			updateArc(progressArc4, label4);
		}
	}

	private void updateArc(Arc arc, Label label) {
		if (arc != null) {
			arc.setLength(-progress * 3.6); // Convert 100% to 360 degrees
		}
		if (label != null) {
			label.setText((int) progress + "%");
		}
	}



	@FXML
	public void goToProfile(ActionEvent event) throws IOException {
		profileController.setPaneContainer(paneContainer);
		profileController.goToProfile(event);
	}

	@FXML
	public void goToTrack(ActionEvent event) throws IOException {
		mapController.setPaneContainer(paneContainer);
		mapController.goToMap(event);
	}

	@FXML
	public void goToWorkoutGoals(ActionEvent event) throws IOException{
		workoutAndGoalsController.setPaneContainer(paneContainer);
		workoutAndGoalsController.switchToWaG(event);
	}

	@FXML
	public void goToNutrition(ActionEvent event) throws IOException {
		nutritionController.setPaneContainer(paneContainer);
		nutritionController.switchToNutrition(event);
	}

	@FXML
	public void toRegister(ActionEvent event) throws IOException {
		registerController.setPaneContainer(paneContainer);
		registerController.toRegister(event);
	}

//	@FXML
//	public void goToMap(ActionEvent event) throws IOException {
//		mapController.setPaneContainer(paneContainer);
//		mapController.switchToMap1(event);
//	}

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