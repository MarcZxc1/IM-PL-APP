package com.dev.marc.fitnesstrackingapplication.controller;

import com.dev.marc.fitnesstrackingapplication.utils.TabSwitch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.io.IOException;

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
    private Pane paneContainer;

    @FXML
    private Button saveButton;

    public void setPaneContainer(Pane paneContainer) {this.paneContainer = paneContainer;}

    private static final String VIEW_PATH = "/com/dev/marc/fitnesstrackingapplication/view/";

    @FXML
    public void initialize() {
        // Correct way to set ComboBox items
        genderComboBox.getItems().addAll("Male", "Female", "Other");

//        saveButton.setOnAction(event -> saveProfileSettings());
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

    @FXML
    public void goToSettings(ActionEvent event) throws IOException {
        TabSwitch.switchTab(paneContainer, VIEW_PATH + "Settings.fxml",
                1250, 680, false );
    }
}
