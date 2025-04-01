package com.dev.marc.fitnesstrackingapplication.utils;

import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;

import java.util.Objects;

public class PasswordVisibilityHandler {
	private final PasswordField passwordField;
	private final TextField visiblePasswordField;
	private final ImageView eyeIcon;

	private static final String ASSETS_PATH = "/assets/";

	public PasswordVisibilityHandler(PasswordField passwordField, TextField visiblePasswordField, ImageView eyeIcon) {
		this.passwordField = passwordField;
		this.visiblePasswordField = visiblePasswordField;
		this.eyeIcon = eyeIcon;

		// ✅ Initially hide the visible password field
		this.visiblePasswordField.setManaged(false);
		this.visiblePasswordField.setVisible(false);

		// ✅ Set default eye icon
		this.eyeIcon.setImage(loadImageSafely(ASSETS_PATH + "hide.png"));

		// ✅ Ensure text is synced between fields
		this.visiblePasswordField.textProperty().bindBidirectional(this.passwordField.textProperty());
	}

	public void showPassword() {
		visiblePasswordField.setManaged(true);
		visiblePasswordField.setVisible(true);
		passwordField.setManaged(false);
		passwordField.setVisible(false);
		eyeIcon.setImage(loadImageSafely(ASSETS_PATH + "view.png"));
	}

	public void hidePassword() {
		passwordField.setManaged(true);
		passwordField.setVisible(true);
		visiblePasswordField.setManaged(false);
		visiblePasswordField.setVisible(false);
		eyeIcon.setImage(loadImageSafely(ASSETS_PATH + "hide.png"));
	}

	private Image loadImageSafely(String path) {
		try {
			return new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
		} catch (Exception e) {
			System.err.println("Error: Could not load image " + path);
			return null;
		}
	}
}
