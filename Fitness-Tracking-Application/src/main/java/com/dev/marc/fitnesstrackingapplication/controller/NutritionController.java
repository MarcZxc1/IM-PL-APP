package com.dev.marc.fitnesstrackingapplication.controller;

import com.dev.marc.fitnesstrackingapplication.utils.TabSwitch;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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


//	@FXML private Label mealNameLabel;
//	@FXML private TextArea ingredientsArea;
//	@FXML private Label caloriesLabel;
//	@FXML private Label proteinLabel;
//	@FXML private Label carbsLabel;
//	@FXML private Label fatLabel;
//	@FXML private TextField mealNameField;
//	@FXML private TextField mealDateField;
//	@FXML private TextArea mealDescriptionField;
//	@FXML private TextField proteinField;
//	@FXML private TextField caloriesField;
//	@FXML private Button saveButton;

	@FXML Label mealNameLabel;
	@FXML Label mealNameLabe2;
	@FXML Label mealNameLabe3;
	@FXML Label mealNameLabe4;


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
		String base = "http://localhost:8080/api/meals/suggest?ingredient=";
		String query = URLEncoder.encode(searchField.getText(), StandardCharsets.UTF_8);

		fetchAndDisplayMeal(base + query + "+bulking", mealSuggestion1, mealBackPane1, mealImage1);
		fetchAndDisplayMeal(base + query + "+cutting", mealSuggestion2, mealBackPane2, mealImage2);
		fetchAndDisplayMeal(base + query + "+maintenance", mealSuggestion3, mealBackPane3, mealImage3);
		fetchAndDisplayMeal(base + query + "+lose+weight", mealSuggestion4, mealBackPane4, mealImage4);
	}

	private void fetchAndDisplayMeal(String url, Pane front, Pane back, ImageView imageView) {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.build();

		client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
				.thenApply(HttpResponse::body)
				.thenAccept(response -> Platform.runLater(() -> displayMealOnPane(response, front, back, imageView)))
				.exceptionally(ex -> {
					Platform.runLater(() -> resultArea.setText("Error: " + ex.getMessage()));
					return null;
				});
	}

	private void displayMealOnPane(String json, Pane frontPane, Pane backPane, ImageView imageView) {
		try {
			JSONObject data = new JSONObject(json);

			String mealName = data.getString("meal");

			// Front - Name and Image
			Label nameLabel = (Label) frontPane.lookup("#mealNameLabel"); // Replace with your actual fx:id if unique
			if (nameLabel != null) nameLabel.setText("Meal: " + mealName);

			String fileName = mealName.toLowerCase().replace(" ", "-") + ".jpg";
			String imagePath = "/assets/FoodImages/" + fileName;
			updateMealImage(imageView, imagePath);

			// Back - Ingredients and Nutrition
			TextArea ingredientsArea = (TextArea) backPane.lookup("#ingredientsArea");
			JSONArray ingredientsArray = data.getJSONArray("ingredients");
			StringBuilder ingredientText = new StringBuilder();
			for (int i = 0; i < ingredientsArray.length(); i++) {
				ingredientText.append("_ ").append(ingredientsArray.getString(i)).append("\n");
			}
			if (ingredientsArea != null) ingredientsArea.setText(ingredientText.toString());

			JSONObject nutrition = data.getJSONObject("nutrition");
			Label cal = (Label) backPane.lookup("#caloriesLabel");
			Label pro = (Label) backPane.lookup("#proteinLabel");
			Label carbs = (Label) backPane.lookup("#carbsLabel");
			Label fat = (Label) backPane.lookup("#fatLabel");

			if (cal != null) cal.setText("Calories: " + nutrition.getInt("calories") + " kcal");
			if (pro != null) pro.setText("Protein: " + nutrition.getInt("protein") + " g");
			if (carbs != null) carbs.setText("Carbs: " + nutrition.optInt("carbohydrates", 0) + " g");
			if (fat != null) fat.setText("Fat: " + nutrition.optInt("fat", 0) + " g");

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







//	private void saveMeal() {
//		String mealName = mealNameField.getText();
//		String mealDate = mealDateField.getText();
//		String description = mealDescriptionField.getText();
//		String protein = proteinField.getText();
//		String calories = caloriesField.getText();
//
//		System.out.println("Meal saved: " + mealName);
//
//		mealNameField.clear();
//		mealDateField.clear();
//		mealDescriptionField.clear();
//		proteinField.clear();
//		caloriesField.clear();
//	}

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

