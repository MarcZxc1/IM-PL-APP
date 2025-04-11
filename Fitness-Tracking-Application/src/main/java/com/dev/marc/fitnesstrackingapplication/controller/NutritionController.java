package com.dev.marc.fitnesstrackingapplication.controller;

import com.dev.marc.fitnesstrackingapplication.utils.TabSwitch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class NutritionController {



	@FXML private TextField mealNameField;
	@FXML private TextField mealDateField;
	@FXML private TextArea mealDescriptionField;
	@FXML private TextField proteinField;
	@FXML private TextField caloriesField;
	@FXML private Button saveButton;
	@FXML private ImageView mealImage1;
	@FXML private ImageView mealImage2;
	@FXML private ImageView mealImage3;

	ProfileController profileController = new ProfileController();
//	NutritionController nutritionController = new NutritionController();
	WorkoutAndGoalsController workoutAndGoalsController = new WorkoutAndGoalsController();
	MetricsController metricsController = new MetricsController();
	ReminderController reminderController = new ReminderController();
	ReportController reportController = new ReportController();
	SettingsController settingsController = new SettingsController();

	@FXML
	private Pane paneContainer;

	private static final String VIEW_PATH = "/com/dev/marc/fitnesstrackingapplication/view/";

	public void setPaneContainer(Pane paneContainer) {this.paneContainer = paneContainer;}

	@FXML
	public void goToNutrition(ActionEvent event) throws IOException {
		TabSwitch.switchTab(paneContainer, VIEW_PATH + "Nutrition.fxml",
				100, 500, false );
	}

	@FXML
	private void Profile(ActionEvent event) throws IOException {
		profileController.setPaneContainer(paneContainer);
		profileController.goToProfile(event);}


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


	public void initialize() {
		// Initialize any default values or event handlers here
		saveButton.setOnAction(event -> saveMeal());
	}

	private void saveMeal() {
		// Handle saving the meal information
		String mealName = mealNameField.getText();
		String mealDate = mealDateField.getText();
		String description = mealDescriptionField.getText();
		String protein = proteinField.getText();
		String calories = caloriesField.getText();

		// Here you would typically save to a database or file
		System.out.println("Meal saved: " + mealName);

		// Clear fields after saving
		mealNameField.clear();
		mealDateField.clear();
		mealDescriptionField.clear();
		proteinField.clear();
		caloriesField.clear();
	}

	// Method to update meal images (to be called later when you implement image upload)
	public void updateMealImage(ImageView imageView, String imagePath) {
		try {
			Image image = new Image(getClass().getResourceAsStream(imagePath));
			imageView.setImage(image);
		} catch (Exception e) {
			System.err.println("Error loading image: " + e.getMessage());
		}
	}
}