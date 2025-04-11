package com.dev.marc.fitnesstrackingapplication.controller;

import com.dev.marc.fitnesstrackingapplication.utils.TabSwitch;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ReportController {

    @FXML
    private ComboBox<String> reportTypeComboBox;

    @FXML
    private Button generateReportButton;

    @FXML
    private Label totalStepsLabel;

    @FXML
    private Label totalWorkoutsLabel;

    @FXML
    private Pane paneContainer;

    private static final String VIEW_PATH = "/com/dev/marc/fitnesstrackingapplication/view/";

    public void setPaneContainer(Pane paneContainer) {this.paneContainer = paneContainer;}

    @FXML
    public void goToReport(ActionEvent event) throws IOException {
        TabSwitch.switchTab(paneContainer, VIEW_PATH + "Report.fxml",
                1250, 680, false );
    }
//
    @FXML
    public void initialize() {
        // Populate the report type combo box
//        reportTypeComboBox.getItems().addAll("Weekly", "Monthly");
    }

    @FXML
    private void handleGenerateReport(ActionEvent event) {
        String selectedType = reportTypeComboBox.getValue();

        if (selectedType == null || selectedType.isEmpty()) {
            showAlert("No Selection", "Please select a report type (Weekly or Monthly).");
            return;
        }

        // Simulate fetching report data
        if (selectedType.equals("Weekly")) {
            totalStepsLabel.setText("32,560");
            totalWorkoutsLabel.setText("5");
        } else if (selectedType.equals("Monthly")) {
            totalStepsLabel.setText("140,230");
            totalWorkoutsLabel.setText("21");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
