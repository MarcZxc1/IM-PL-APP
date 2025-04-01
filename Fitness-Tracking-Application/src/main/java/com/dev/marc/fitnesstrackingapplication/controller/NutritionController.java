package com.dev.marc.fitnesstrackingapplication.controller;

import com.dev.marc.fitnesstrackingapplication.utils.TabSwitch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class NutritionController {

	ProfileController profileController = new ProfileController();

	@FXML
	AnchorPane paneContainer;

	private static String VIEW_PATH = "/com/dev/marc/fitnesstrackingapplication/view/";

	public void setPaneContainer(AnchorPane paneContainer) {
		this.paneContainer = paneContainer;
	}

	public void switchToNutrition(ActionEvent event) throws IOException {
		TabSwitch.switchTab(paneContainer,VIEW_PATH + "Nutrition.fxml",
				1250, 680, false);

	}



	@FXML
	public void HOME(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(VIEW_PATH + "Dashboard.fxml"));
		Parent root = loader.load();

		// Get the current scene and update it
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(new Scene(root, 1250, 680));
		stage.show();
	}


}
