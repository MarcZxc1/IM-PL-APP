package com.dev.marc.fitnesstrackingapplication.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SettingsController {

    @FXML
    private TextField locationField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField emailField;

    @FXML
    private DatePicker dobPicker;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private Button saveButton;

    @FXML
    public void initialize() {
        // Correct way to set ComboBox items
        genderComboBox.getItems().addAll("Male", "Female", "Other");

        saveButton.setOnAction(event -> saveProfileSettings());
    }

    private void saveProfileSettings() {
        String location = locationField.getText();
        String address = addressField.getText();
        String email = emailField.getText();
        String dob = (dobPicker.getValue() != null) ? dobPicker.getValue().toString() : "Not selected";
        String gender = genderComboBox.getSelectionModel().getSelectedItem();

        System.out.println("Profile Updated:");
        System.out.println("Location: " + location);
        System.out.println("Address: " + address);
        System.out.println("Email: " + email);
        System.out.println("Date of Birth: " + dob);
        System.out.println("Gender: " + gender);
    }
}
