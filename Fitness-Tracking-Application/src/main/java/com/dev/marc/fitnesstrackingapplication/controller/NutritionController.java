package com.dev.marc.fitnesstrackingapplication.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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