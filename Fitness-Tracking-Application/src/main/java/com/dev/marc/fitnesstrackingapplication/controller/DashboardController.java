package com.dev.marc.fitnesstrackingapplication.controller;

import com.dev.marc.fitnesstrackingapplication.utils.SceneSwitcher;
import com.dev.marc.fitnesstrackingapplication.utils.TabSwitch;
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
import javafx.scene.layout.Pane;
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
	public Pane paneContainer;


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

	ProfileController profileController = new ProfileController();
	NutritionController nutritionController = new NutritionController();
	WorkoutAndGoalsController workoutAndGoalsController = new WorkoutAndGoalsController();
	MetricsController metricsController = new MetricsController();
	ReminderController reminderController = new ReminderController();
	ReportController reportController = new ReportController();
	SettingsController settingsController = new SettingsController();

	public void setPaneContainer(Pane paneContainer) {this.paneContainer = paneContainer;}

	private static final String VIEW_PATH = "/com/dev/marc/fitnesstrackingapplication/view/";

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
	private void Profile(ActionEvent event) throws IOException {
		profileController.setPaneContainer(paneContainer);
		profileController.goToProfile(event);}

	@FXML
	private void Nutrition(ActionEvent event) throws IOException {
		nutritionController.setPaneContainer(paneContainer);
		nutritionController.goToNutrition(event);}

	@FXML
	private void WorkoutANDGoals(ActionEvent event) throws IOException {
		workoutAndGoalsController.setPaneContainer(paneContainer);
		workoutAndGoalsController.goToWorkoutAndGoals(event);}

	@FXML
	private void Metrics(ActionEvent event) throws IOException {
		metricsController.setPaneContainer(paneContainer);
		metricsController.goToMetrics(event);}

	@FXML
	private void Reminder(ActionEvent event) throws IOException {
		reminderController.setPaneContainer(paneContainer);
		reminderController.goToReminder(event);}


	@FXML
	private void Report(ActionEvent event) throws IOException {
		reportController.setPaneContainer(paneContainer);
		reportController.goToReport(event);}

	@FXML
	private void Settings(ActionEvent event) throws IOException {
		settingsController.setPaneContainer(paneContainer);
		settingsController.goToSettings(event);}






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

	@FXML
	public void homeButton(ActionEvent event) throws IOException {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		SceneSwitcher.switchScene(stage, VIEW_PATH + "Dashboard.fxml",
				1315, 740, false);
	}

}
