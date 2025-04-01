package com.dev.marc.fitnesstrackingapplication.controller;

import com.dev.marc.fitnesstrackingapplication.utils.PasswordUtils;
import com.dev.marc.fitnesstrackingapplication.utils.PasswordVisibilityHandler;
import com.dev.marc.fitnesstrackingapplication.utils.TabSwitch;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartUpController implements Initializable {
	@FXML private Label label;
	@FXML private ImageView userImageView;
	@FXML private Label userInfoLabel;
	@FXML private AnchorPane paneContainer;
	@FXML private TextField username;
	@FXML private PasswordField passwordField;
	@FXML private TextField visiblePasswordField;
	@FXML private CheckBox showPasswordCheckBox;
	@FXML private ImageView eyeIcon;
	@FXML private Button signup;
	@FXML private WebView webView;

	RegisterController registerController = new RegisterController();

	private static final String VIEW_PATH = "/com/dev/marc/fitnesstrackingapplication/view";
	private PasswordVisibilityHandler passwordVisibilityHandler;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

		passwordVisibilityHandler = new PasswordVisibilityHandler(passwordField, visiblePasswordField, eyeIcon);
		passwordVisibilityHandler.hidePassword();

		showPasswordCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				passwordVisibilityHandler.showPassword();
			} else {
				passwordVisibilityHandler.hidePassword();
			}
		});

	}

	@FXML
	private void handleGoogleLogin(ActionEvent event) {
		System.out.println("🌐 Opening Google OAuth login in external browser...");
		try {
			Desktop.getDesktop().browse(new URI("http://localhost:8080/auth/google-login"));
			//we will use a timer, to check if the user has logged in,
			//and then load the register scene.
			new java.util.Timer().schedule(
					new java.util.TimerTask() {
						@Override
						public void run() {
							Platform.runLater(() -> {
								try {
									switchToRegisterScene();
								} catch (IOException e) {
									e.printStackTrace();
								}
							});
						}
					},
					5000 // 5 seconds.
			);
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void switchToRegisterScene() throws IOException {
		if (paneContainer == null) {
			System.out.println("⚠️ paneContainer is NULL! Using Stage instead...");
			switchSceneWithStage(VIEW_PATH + "/Register.fxml");
			return;
		}

		System.out.println("🔄 Switching to Register.fxml using paneContainer...");
		TabSwitch.switchTab(paneContainer, VIEW_PATH + "/Register.fxml", 800, 600, false);
	}

	private void switchSceneWithStage(String fxmlPath) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
		Parent root = loader.load();

		Stage stage = (Stage) label.getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	public void login(ActionEvent event) {
		String email = username.getText().trim();
		String password = passwordField.getText().trim();

		if (email.isEmpty() || password.isEmpty()) {
			showAlert("Login Failed", "Please enter both email and password.");
			return;
		}

		// ... (Your login logic remains the same)
	}

	@FXML
	public void toRegister(ActionEvent event) throws IOException {
		registerController.setPaneContainer(paneContainer);
		registerController.toRegister(event);
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
		System.err.println(message);
	}
}