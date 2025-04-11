package com.dev.marc.fitnesstrackingapplication.controller;

import com.dev.marc.fitnesstrackingapplication.utils.TabSwitch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.time.LocalDate;

public class ProfileController {

	private static final String VIEW_PATH = "/com/dev/marc/fitnesstrackingapplication/view/";
	@FXML private Pane paneContainer;




//	@FXML
//	private TextField nameField;
//
//	@FXML
//	private TextField usernameField;
//
//	@FXML
//	private TextField emailField;
//
//	@FXML
//	private ComboBox<String> genderComboBox;
//
//	@FXML
//	private DatePicker dobDatePicker;
//
//	@FXML
//	private TextField addressField;
//
//	@FXML private AnchorPane paneContainer;
//
//	public void setPaneContainer(AnchorPane paneContainer) {this.paneContainer = paneContainer;}
//
//	// Called when the profile form is loaded
//	public void initialize() {
//		// Set default values if available (for example, from a user object or a database)
//		loadUserProfile();
//		handleSaveProfile();
//	}
//
//	private void loadUserProfile() {
//		// Example data (replace this with actual data from a database or settings form)
//		nameField.setText("Jane Doe");
//		usernameField.setText("janedoe123");
//		emailField.setText("jane.doe@example.com");
//		genderComboBox.getItems().addAll("Male", "Female", "Other");
//		genderComboBox.setValue("Female");
//		dobDatePicker.setValue(LocalDate.of(1999, 5, 15));  // Example date of birth
//		addressField.setText("123 Main Street, City, Country");
//	}
//
//	// Handle form submission (for example, save the data to a database or settings file)
//	@FXML
//	private void handleSaveProfile() {
//		String name = nameField.getText();
//		String username = usernameField.getText();
//		String email = emailField.getText();
//		String gender = genderComboBox.getValue();
//		LocalDate dob = dobDatePicker.getValue();
//		String address = addressField.getText();
//
//		// Process the collected data (save it to a database, settings file, etc.)
//		System.out.println("Profile saved:");
//		System.out.println("Name: " + name);
//		System.out.println("Username: " + username);
//		System.out.println("Email: " + email);
//		System.out.println("Gender: " + gender);
//		System.out.println("Date of Birth: " + dob);
//		System.out.println("Address: " + address);
//	}



	public void setPaneContainer(Pane paneContainer) {this.paneContainer = paneContainer;}

	@FXML
	public void goToProfile(ActionEvent event) throws IOException {
		TabSwitch.switchTab(paneContainer, VIEW_PATH + "Profile.fxml",
				950, 500, false );
	}
}
