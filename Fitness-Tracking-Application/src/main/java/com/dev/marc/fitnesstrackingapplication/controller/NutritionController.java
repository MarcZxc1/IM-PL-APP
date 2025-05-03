package com.dev.marc.fitnesstrackingapplication.controller;

import com.dev.marc.fitnesstrackingapplication.utils.TabSwitch;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class NutritionController {


	@FXML ComboBox mealTypeComboBox;

	@FXML private Label caloriesLabel;
	@FXML private Label proteinLabel;
	@FXML private Label carbsLabel;
	@FXML private Label fatLabel;

	@FXML private Label caloriesLabel2;
	@FXML private Label proteinLabel2;
	@FXML private Label carbsLabel2;
	@FXML private Label fatLabel2;

	@FXML private Label caloriesLabel3;
	@FXML private Label proteinLabel3;
	@FXML private Label carbsLabel3;
	@FXML private Label fatLabel3;

	@FXML private Label caloriesLabel4;
	@FXML private Label proteinLabel4;
	@FXML private Label carbsLabel4;
	@FXML private Label fatLabel4;




	@FXML Label mealNameLabel1;
	@FXML Label mealNameLabel2;
	@FXML Label mealNameLabel3;
	@FXML Label mealNameLabel4;


	private boolean isFlipped = false;
	@FXML Pane mealSuggestion1;
	@FXML Pane mealSuggestion4;

	@FXML Pane mealBackPane1;
	@FXML Pane mealBackPane2;
	@FXML Pane mealBackPane3;

	@FXML Pane mealSuggestion2;
	@FXML Pane mealSuggestion3;
	@FXML Pane mealBackPane4;



	// Removed Pexels-related images
	@FXML private ImageView mealImage1;
	@FXML private ImageView mealImage2;
	@FXML private ImageView mealImage3;
	@FXML private ImageView mealImage4;

	@FXML private Pane paneContainer;

	private static final String VIEW_PATH = "/com/dev/marc/fitnesstrackingapplication/view/";

	public void setPaneContainer(Pane paneContainer) {
		this.paneContainer = paneContainer;
	}

	@FXML
	public void goToNutrition(ActionEvent event) throws IOException {
		TabSwitch.switchTab(paneContainer, VIEW_PATH + "Nutrition.fxml",
				1095, 500, false);
	}

	@FXML private TextField searchField;
	@FXML private TextArea resultArea;
	@FXML private Button searchButton;

	@FXML
	public void initialize() {
		searchButton.setOnAction(e -> searchFood());
		mealTypeComboBox.getItems().addAll("bulking", "cutting", "maintenance", "lose weight");

		// Default visibility
		setInitialPaneState(mealSuggestion1, mealBackPane1);
		setInitialPaneState(mealSuggestion2, mealBackPane2);
		setInitialPaneState(mealSuggestion3, mealBackPane3);
		setInitialPaneState(mealSuggestion4, mealBackPane4);

		// Flip handlers
		mealSuggestion1.setOnMouseClicked(e -> flipMealPane(mealSuggestion1, mealBackPane1));
		mealBackPane1.setOnMouseClicked(e -> flipMealPane(mealSuggestion1, mealBackPane1));

		mealSuggestion2.setOnMouseClicked(e -> flipMealPane(mealSuggestion2, mealBackPane2));
		mealBackPane2.setOnMouseClicked(e -> flipMealPane(mealSuggestion2, mealBackPane2));

		mealSuggestion3.setOnMouseClicked(e -> flipMealPane(mealSuggestion3, mealBackPane3));
		mealBackPane3.setOnMouseClicked(e -> flipMealPane(mealSuggestion3, mealBackPane3));

		mealSuggestion4.setOnMouseClicked(e -> flipMealPane(mealSuggestion4, mealBackPane4));
		mealBackPane4.setOnMouseClicked(e -> flipMealPane(mealSuggestion4, mealBackPane4));
	}

	private void setInitialPaneState(Pane front, Pane back) {
		front.setVisible(true);
		back.setVisible(false);

		// Make sure back is in the same parent if not already
		if (!((Pane) front.getParent()).getChildren().contains(back)) {
			((Pane) front.getParent()).getChildren().add(back);
		}
	}



	private void searchFood() {
		String ingredient = searchField.getText().trim();
		String mealType = (String) mealTypeComboBox.getValue(); // Get the selected meal type

		if (ingredient.isEmpty()) {
			resultArea.setText("Please enter an ingredient.");
			return;
		}

		String url = "http://localhost:8080/api/meals/suggest?ingredient=" + URLEncoder.encode(ingredient, StandardCharsets.UTF_8);
		if (mealType != null && !mealType.isEmpty()) {
			url += "&mealType=" + URLEncoder.encode(mealType, StandardCharsets.UTF_8);
		}

		System.out.println("Encoded URL: " + url);
		fetchAndDisplayMeals(url);


		if (mealType != null && !mealType.isEmpty()) {
			String encodedMealType = URLEncoder.encode(mealType, StandardCharsets.UTF_8);
			url += "&mealType=" + encodedMealType;
		}

		System.out.println("Encoded URL: " + url); // Debug log

		// Call API and display results on 4 panes
		fetchAndDisplayMeals(url);

	}


	private void fetchAndDisplayMeals(String url) { HttpClient client = HttpClient.newHttpClient(); HttpRequest request = HttpRequest.newBuilder() .uri(URI.create(url)) .build();
		client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
				.thenApply(HttpResponse::body)
				.thenAccept(response -> Platform.runLater(() -> {
					try {
						JSONArray array = new JSONArray(response);

						if (array.length() > 0) displayMealOnPane(String.valueOf(array.getJSONObject(0)), mealSuggestion1, mealBackPane1, mealImage1, mealNameLabel1);
						if (array.length() > 1) displayMealOnPane(String.valueOf(array.getJSONObject(1)), mealSuggestion2, mealBackPane2, mealImage2, mealNameLabel2);
						if (array.length() > 2) displayMealOnPane(String.valueOf(array.getJSONObject(2)), mealSuggestion3, mealBackPane3, mealImage3, mealNameLabel3);
						if (array.length() > 3) displayMealOnPane(String.valueOf(array.getJSONObject(3)), mealSuggestion4, mealBackPane4, mealImage4, mealNameLabel4);


						if (array.length() == 0) {
							resultArea.setText("No meals found.");
						}
					} catch (Exception e) {
						resultArea.setText("Failed to parse meal suggestions.");
						e.printStackTrace();
					}
				}))
				.exceptionally(ex -> {
					Platform.runLater(() -> resultArea.setText("Error: " + ex.getMessage()));
					return null;
				});

	}


	private void displayMealOnPane(String json, Pane frontPane, Pane backPane, ImageView imageView, Label mealNameLabel) {
		try {
			System.out.println("Raw JSON response: " + json);

			// Safeguard: Ensure the response is a JSON object
			if (json == null || !json.trim().startsWith("{")) {
				resultArea.setText("Invalid response from server.");
				System.err.println("Unexpected response format: " + json);
				return;
			}

			JSONObject data = new JSONObject(json);

			String mealName = data.getString("meal");
			if (mealNameLabel != null) {
				mealNameLabel.setText(mealName);
			}

			String fileName = mealName.toLowerCase().replace(" ", "_") + ".jpg";



			String imagePath = "/assets/FoodImages/" + fileName ;

			updateMealImage(imageView, imagePath);


			TextArea ingredientsArea = (TextArea) backPane.lookup("#ingredientsArea");
			if (ingredientsArea != null && data.has("ingredients")) {
				JSONArray ingredientsArray = data.getJSONArray("ingredients");
				StringBuilder ingredientText = new StringBuilder();
				for (int i = 0; i < ingredientsArray.length(); i++) {
					ingredientText.append("_ ").append(ingredientsArray.getString(i)).append("\n");
				}
				ingredientsArea.setText(ingredientText.toString());
			}

			JSONObject nutrition = data.optJSONObject("nutrition");
			if (nutrition == null) return;

			Label caloriesLabelLocal = null;
			Label proteinLabelLocal = null;
			Label carbsLabelLocal = null;
			Label fatLabelLocal = null;

			if (backPane == mealBackPane1) {
				caloriesLabelLocal = caloriesLabel;
				proteinLabelLocal = proteinLabel;
				carbsLabelLocal = carbsLabel;
				fatLabelLocal = fatLabel;
			} else if (backPane == mealBackPane2) {
				caloriesLabelLocal = caloriesLabel2;
				proteinLabelLocal = proteinLabel2;
				carbsLabelLocal = carbsLabel2;
				fatLabelLocal = fatLabel2;
			} else if (backPane == mealBackPane3) {
				caloriesLabelLocal = caloriesLabel3;
				proteinLabelLocal = proteinLabel3;
				carbsLabelLocal = carbsLabel3;
				fatLabelLocal = fatLabel3;
			} else if (backPane == mealBackPane4) {
				caloriesLabelLocal = caloriesLabel4;
				proteinLabelLocal = proteinLabel4;
				carbsLabelLocal = carbsLabel4;
				fatLabelLocal = fatLabel4;
			}

			if (caloriesLabelLocal != null)
				caloriesLabelLocal.setText("Calories: " + nutrition.optInt("calories", 0) + " kcal");
			if (proteinLabelLocal != null)
				proteinLabelLocal.setText("Protein: " + nutrition.optInt("protein", 0) + " g");
			if (carbsLabelLocal != null)
				carbsLabelLocal.setText("Carbs: " + nutrition.optInt("carbohydrates", 0) + " g");
			if (fatLabelLocal != null)
				fatLabelLocal.setText("Fat: " + nutrition.optInt("fat", 0) + " g");

		} catch (Exception e) {
			resultArea.setText("Error displaying meal.");
			e.printStackTrace();
		}
	}


	private void flipMealPane(Pane front, Pane back) {
		Pane showing = front.isVisible() ? front : back;
		Pane hidden = front.isVisible() ? back : front;

		RotateTransition rotateOut = new RotateTransition(Duration.millis(300), showing);
		rotateOut.setAxis(Rotate.Y_AXIS);
		rotateOut.setFromAngle(0);
		rotateOut.setToAngle(90);

		RotateTransition rotateIn = new RotateTransition(Duration.millis(300), hidden);
		rotateIn.setAxis(Rotate.Y_AXIS);
		rotateIn.setFromAngle(-90);
		rotateIn.setToAngle(0);

		rotateOut.setOnFinished(e -> {
			showing.setVisible(false);
			hidden.setVisible(true);
			rotateIn.play();
		});

		rotateOut.play();
	}

	public void updateMealImage(ImageView mealImage1, String imagePath) {
		try {
			InputStream stream = getClass().getResourceAsStream(imagePath);
			if (stream == null) {
				System.err.println("Image not found: " + imagePath);
				return;
			}
			mealImage1.setImage(new javafx.scene.image.Image(stream));
			System.out.println("Image loaded successfully: " + imagePath);
		} catch (Exception e) {
			System.err.println("Error loading image: " + e.getMessage());
		}
	}



}