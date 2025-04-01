package com.dev.marc.fitnesstrackingapplication.controller;

import com.dev.marc.fitnesstrackingapplication.utils.TabSwitch;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Arc;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import java.io.IOException;

public class ProfileController {
	@FXML private ProgressBar progressBar;
	@FXML private Arc progressArc;  // 🔹 Now properly linked
	@FXML private Label percentageLabel;
	private static final String VIEW_PATH = "/com/dev/marc/fitnesstrackingapplication/view/";

	@FXML private AnchorPane paneContainer;


	public void setPaneContainer(AnchorPane paneContainer) {
		this.paneContainer = paneContainer;
	}

	private double progress = 0; // Start at 0%

	public void initialize() {
		// Initialize Arc
		if (progressArc != null) {  // 🔹 Avoid null issues
			progressArc.setStartAngle(90);
		}

		// Animate progress
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

			// 🔹 Update Arc smoothly
			if (progressArc != null) {
				progressArc.setLength(-progress * 3.6);
			}

			percentageLabel.setText((int) progress + "%");
		}
	}

	@FXML
	public void goToProfile(ActionEvent event) throws IOException {
		if (paneContainer != null) {
			TabSwitch.switchTab(paneContainer, VIEW_PATH + "Profile.fxml", 1250,
					680, true);
		} else {
			System.out.println("Error: paneContainer is null!");
		}
	}

	@FXML
	public void HOME(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(VIEW_PATH + "Dashboard.fxml"));
		Parent root = loader.load();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(new Scene(root, 1250, 680));
		stage.show();
	}

}
