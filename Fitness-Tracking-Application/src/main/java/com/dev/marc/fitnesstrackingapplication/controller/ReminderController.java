package com.dev.marc.fitnesstrackingapplication.controller;  // Ensure this matches your folder structure

import com.dev.marc.fitnesstrackingapplication.utils.TabSwitch;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ReminderController {

    public void initialize() {
        var url = getClass().getResource("/Icons/Dashboard.png");
        System.out.println("Image URL: " + url);
        if (url == null) {
            System.out.println("❌ Image not found!");
        } else {
            System.out.println("✅ Image found!");
        }
    }


    @FXML
    private DatePicker reminderDatePicker; // Date picker for selecting the reminder date

    @FXML
    private TextArea reminderNotesField; // Text area for entering reminder notes

    @FXML
    private Button saveReminderButton; // Button for saving the reminder

    @FXML
    private Pane paneContainer;

    private static final String VIEW_PATH = "/com/dev/marc/fitnesstrackingapplication/view/";

    public void setPaneContainer(Pane paneContainer) {this.paneContainer = paneContainer;}

    @FXML
    public void goToReminder(ActionEvent event) throws IOException {
        TabSwitch.switchTab(paneContainer, VIEW_PATH + "Reminder.fxml",
                1250, 680, false );
    }


    /**
     * This method handles the action of saving the reminder when the button is clicked.
     */
    @FXML
    private void handleSaveReminder(ActionEvent event) {
        // Get the selected date and notes
        String selectedDate = reminderDatePicker.getValue() != null ? reminderDatePicker.getValue().toString() : null;
        String reminderNotes = reminderNotesField.getText();

        // Check if the fields are filled out correctly
        if (selectedDate == null || selectedDate.isEmpty()) {
            showAlert("Error", "Please select a date for your reminder.");
            return;
        }

        if (reminderNotes == null || reminderNotes.isEmpty()) {
            showAlert("Error", "Please enter some notes for the reminder.");
            return;
        }

        // Save the reminder (In this case, we are just showing an alert)
        // You can save it to a database or a file depending on your application requirements

        // Show success alert
        showAlert("Success", "Your reminder has been saved!");
    }

    /**
     * This method shows a popup alert with a given title and message.
     *
     * @param title   the title of the alert
     * @param message the message to be displayed
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
