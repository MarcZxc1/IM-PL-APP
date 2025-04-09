package com.dev.marc.fitnesstrackingapplication.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MetricsController {

    @FXML
    private TextField ageFieldProtein, genderFieldProtein, heightFieldProtein, weightFieldProtein;
    @FXML
    private ComboBox<String> activityComboBox;
    @FXML
    private TextArea proteinResultArea;

    @FXML
    private void initialize() {
        // Populate activityComboBox
        activityComboBox.getItems().addAll(
                "Sedentary: Little or no exercise",
                "Light: Exercise 1-3 times/week",
                "Moderate: Exercise 4-5 times/week",
                "Active: Daily exercise or intense exercise 3-4 times/week",
                "Very Active: Intense exercise 6-7 times/week",
                "Extra Active: Very intense exercise daily or physical job"
        );
    }

    @FXML
    private void handleCalculateProtein() {
        try {
            // Get values from input fields
            double weight = Double.parseDouble(weightFieldProtein.getText());
            String activityLevel = activityComboBox.getValue();

            // Define activity factor based on selected activity level
            double activityFactor = 0.8; // Default for Sedentary

            switch (activityLevel) {
                case "Light: Exercise 1-3 times/week":
                    activityFactor = 1.0;
                    break;
                case "Moderate: Exercise 4-5 times/week":
                    activityFactor = 1.2;
                    break;
                case "Active: Daily exercise or intense exercise 3-4 times/week":
                    activityFactor = 1.4;
                    break;
                case "Very Active: Intense exercise 6-7 times/week":
                    activityFactor = 1.6;
                    break;
                case "Extra Active: Very intense exercise daily or physical job":
                    activityFactor = 1.8;
                    break;
            }

            // Calculate protein requirement (weight * activity factor)
            double proteinRequirement = weight * activityFactor;

            // Display result in the text area
            proteinResultArea.setText("Your daily protein requirement is: " + proteinRequirement + " grams.");
        } catch (NumberFormatException e) {
            proteinResultArea.setText("Please enter valid numeric values.");
        }
    }
}
