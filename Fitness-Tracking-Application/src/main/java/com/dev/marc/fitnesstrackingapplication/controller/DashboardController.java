package com.dev.marc.fitnesstrackingapplication.controller;

import com.dev.marc.fitnesstrackingapplication.utils.SceneSwitcher;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Arc;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class DashboardController {

	@FXML private ProgressBar progressBar;
	@FXML private Arc progressArc;
	@FXML private Label percentageLabel;
	@FXML private Button TrackBtn;
	@FXML private Button WaGBtn;
	@FXML private Button homeButton;
	@FXML public Pane paneContainer;
	@FXML private Label labell;
	@FXML private Arc progressArc1;
	@FXML private Arc progressArc2;
	@FXML private Arc progressArc3;
	@FXML private Arc progressArc4;
	@FXML private Label label1;
	@FXML private Label label2;
	@FXML private Label label3;
	@FXML private Label label4;

	// Controller instances
	private ProfileController profileController = new ProfileController();
	private NutritionController nutritionController = new NutritionController();
	private WorkoutAndGoalsController workoutAndGoalsController = new WorkoutAndGoalsController();
	private MetricsController metricsController = new MetricsController();
	private ReminderController reminderController = new ReminderController();
	private ReportController reportController = new ReportController();
	private SettingsController settingsController = new SettingsController();
	private MapController mapController = new MapController();

	private double progress = 0;
	private static final String VIEW_PATH = "/com/dev/marc/fitnesstrackingapplication/view/";

	public void setPaneContainer(Pane paneContainer) {
		this.paneContainer = paneContainer;
	}

	public void initialize() {
		initializeArcs();
		setupNavigationHoverEffects();
		setupProgressAnimation();
		applySimpleTypingEffect(labell, "Welcome to Fitness Tracker");
	}

	private void initializeArcs() {
		if (progressArc1 != null) progressArc1.setStartAngle(90);
		if (progressArc2 != null) progressArc2.setStartAngle(90);
		if (progressArc3 != null) progressArc3.setStartAngle(90);
		if (progressArc4 != null) progressArc4.setStartAngle(90);
	}

	private void setupNavigationHoverEffects() {
		paneContainer.lookupAll(".nav-button").forEach(node -> {
			Button button = (Button) node;

			button.setOnMouseEntered(e -> {
				button.setStyle("-fx-background-color: #e0e0e0; -fx-scale-x: 1.02; -fx-scale-y: 1.02;");
			});

			button.setOnMouseExited(e -> {
				button.setStyle("-fx-background-color: transparent; -fx-scale-x: 1.0; -fx-scale-y: 1.0;");
			});
		});
	}

	private void setupProgressAnimation() {
		Timeline timeline = new Timeline(
				new KeyFrame(Duration.seconds(0.05), e -> updateProgress())
		);
		timeline.setCycleCount(100);
		timeline.play();
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
		if (arc != null && label != null) {
			double targetLength = -progress * 3.6;
			arc.setLength(targetLength);
			label.setText((int) progress + "%");

			// Simple pulse effect for milestones
			if (progress % 25 == 0) {
				ScaleTransition st = new ScaleTransition(Duration.millis(200), label);
				st.setFromX(1.0);
				st.setFromY(1.0);
				st.setToX(1.2);
				st.setToY(1.2);
				st.setAutoReverse(true);
				st.setCycleCount(2);
				st.play();
			}
		}
	}

	private void applySimpleTypingEffect(Label label, String text) {
		label.setText(""); // Clear initial text

		Timeline timeline = new Timeline();
		for (int i = 0; i < text.length(); i++) {
			final int index = i;
			timeline.getKeyFrames().add(
					new KeyFrame(Duration.millis(100 * i), e -> {
						label.setText(text.substring(0, index + 1));
					})
			);
		}
		timeline.play();
	}

	// Simplified navigation methods
	@FXML
	private void Profile(ActionEvent event) throws IOException {
		simpleTransition(() -> {
			try {
				profileController.setPaneContainer(paneContainer);
				profileController.goToProfile(event);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@FXML
	private void Nutrition(ActionEvent event) throws IOException {
		simpleTransition(() -> {
			try {
				nutritionController.setPaneContainer(paneContainer);
				nutritionController.goToNutrition(event);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@FXML
	private void WorkoutANDGoals(ActionEvent event) throws IOException {
		simpleTransition(() -> {
			try {
				workoutAndGoalsController.setPaneContainer(paneContainer);
				workoutAndGoalsController.goToWorkoutAndGoals(event);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@FXML
	private void Metrics(ActionEvent event) throws IOException {
		simpleTransition(() -> {
			try {
				metricsController.setPaneContainer(paneContainer);
				metricsController.goToMetrics(event);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@FXML
	private void Reminder(ActionEvent event) throws IOException {
		simpleTransition(() -> {
			try {
				reminderController.setPaneContainer(paneContainer);
				reminderController.goToReminder(event);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@FXML
	private void Report(ActionEvent event) throws IOException {
		simpleTransition(() -> {
			try {
				reportController.setPaneContainer(paneContainer);
				reportController.goToReport(event);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@FXML
	private void Settings(ActionEvent event) throws IOException {
		simpleTransition(() -> {
			try {
				settingsController.setPaneContainer(paneContainer);
				settingsController.goToSettings(event);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	private void simpleTransition(Runnable transitionAction) {
		FadeTransition fade = new FadeTransition(Duration.millis(200), paneContainer);
		fade.setFromValue(1.0);
		fade.setToValue(0.7);
		fade.setOnFinished(e -> {
			transitionAction.run();
			FadeTransition fadeIn = new FadeTransition(Duration.millis(200), paneContainer);
			fadeIn.setFromValue(0.7);
			fadeIn.setToValue(1.0);
			fadeIn.play();
		});
		fade.play();
	}

	@FXML
	public void homeButton(ActionEvent event) throws IOException {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		SceneSwitcher.switchScene(stage, VIEW_PATH + "Dashboard.fxml", 1315, 740, false);
	}

	@FXML
	public void Track(ActionEvent event) throws IOException {
		// Simple scale effect
		ScaleTransition st = new ScaleTransition(Duration.millis(200), paneContainer);
		st.setFromX(1.0);
		st.setFromY(1.0);
		st.setToX(1.05);
		st.setToY(1.05);
		st.setAutoReverse(true);
		st.setCycleCount(2);
		st.setOnFinished(e -> {
			try {
				mapController.setPaneContainer(paneContainer);
				mapController.goToMap(event);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});
		st.play();
	}
}