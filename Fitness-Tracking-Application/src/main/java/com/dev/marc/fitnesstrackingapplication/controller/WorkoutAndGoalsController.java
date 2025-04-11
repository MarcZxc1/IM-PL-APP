package com.dev.marc.fitnesstrackingapplication.controller;

import com.dev.marc.fitnesstrackingapplication.api.ExerciseService;
import com.dev.marc.fitnesstrackingapplication.api.StravaAuthService;
import com.dev.marc.fitnesstrackingapplication.api.StravaFitnessService;
import com.dev.marc.fitnesstrackingapplication.model.Activity;
import com.dev.marc.fitnesstrackingapplication.utils.TabSwitch;
import com.sun.net.httpserver.HttpServer;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class WorkoutAndGoalsController implements Initializable {

	private static String VIEW_PATH = "/com/dev/marc/fitnesstrackingapplication/view/";

	@FXML
	private Button toDashboard;

	@FXML
	private ChoiceBox<String> muscleChoiceBox;

	@FXML
	private Button fetchButton;

	@FXML
	private ListView<String> exerciseListView;

	@FXML
	private Pane paneContainer;

	@FXML
	private TableView<ExerciseService.Exercise> exerciseTableView;

	@FXML
	private TableColumn<ExerciseService.Exercise, String> colName;

	@FXML
	private TableColumn<ExerciseService.Exercise, String> colMuscle;

	@FXML
	private TableColumn<ExerciseService.Exercise, String> colEquipment;

	@FXML
	private TableColumn<ExerciseService.Exercise, String> colDifficulty;

	@FXML
	private TableColumn<ExerciseService.Exercise, String> colInstructions;

	@FXML
	private TableColumn<ExerciseService.Exercise, String> colTips;


	@FXML private TableView<Activity> tableView;
	@FXML private TableColumn<Activity, String> nameColumn;
	@FXML private TableColumn<Activity, String> typeColumn;
	@FXML private TableColumn<Activity, String> distanceColumn;




	@FXML
	public void goToWorkoutAndGoals(ActionEvent event) throws IOException {
		TabSwitch.switchTab(paneContainer, VIEW_PATH + "Workout-and-Goals.fxml",
				1250, 680, false );
	}



	@FXML
	private ImageView exerciseImageView;

	private List<ExerciseService.Exercise> allExercises;

	@FXML private WebView webView;

	@FXML
	private Label stepCountLabel;

	public void setPaneContainer(Pane paneContainer) {this.paneContainer = paneContainer;}


	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		// Populate choice box with muscle groups
		muscleChoiceBox.getItems().addAll(
				"Biceps", "Triceps", "Chest", "Quadriceps", "Hamstrings", "Calves",
				"Lats", "Traps", "Lower_back", "Shoulders"
		);
		muscleChoiceBox.setValue("biceps");

		// Configure table columns to use Exercise properties
		colDifficulty.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDifficulty()));
		colEquipment.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEquipment()));
		colInstructions.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getInstructions()));
		colTips.setCellValueFactory(data -> new SimpleStringProperty("Stay hydrated & maintain good form!")); // Static for now

		// Handle fetch button click
		fetchButton.setOnAction(event -> {
			String selectedMuscle = muscleChoiceBox.getValue();
			allExercises = ExerciseService.getExercises(selectedMuscle); // Fetch all exercises

			// Populate ListView with exercise names
			exerciseListView.getItems().clear();
			for (ExerciseService.Exercise exercise : allExercises) {
				exerciseListView.getItems().add(exercise.getName());
			}

			nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
			typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
			distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));

			authenticateUser();
		});






		// Handle ListView selection to display exercise details in TableView
		exerciseListView.setOnMouseClicked(event -> {
			int selectedIndex = exerciseListView.getSelectionModel().getSelectedIndex();
			if (selectedIndex >= 0) {
				ExerciseService.Exercise selectedExercise = allExercises.get(selectedIndex);


				// Populate TableView with selected exercise's details
				ObservableList<ExerciseService.Exercise> observableExercise = FXCollections.observableArrayList(selectedExercise);
				exerciseTableView.setItems(observableExercise);


				// Load exercise image (if available)
				String imageUrl = selectedExercise.getImageUrl();
				if (imageUrl != null && !imageUrl.isEmpty()) {
					exerciseImageView.setImage(new Image(imageUrl, true));
				} else {
					exerciseImageView.setImage(new Image("https://via.placeholder.com/150")); // Placeholder image
				}
			}
		});
	}

	public void authenticateUser() {
		try {
			int port = 5000; // Change if needed
			HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
			server.createContext("/", exchange -> {
				String query = exchange.getRequestURI().getQuery();
				if (query != null && query.contains("code=")) {
					String code = query.split("code=")[1].split("&")[0];
					try {
						accessToken = StravaAuthService.exchangeCodeForToken(code);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					System.out.println("✅ Access Token: " + accessToken);
					loadActivities(); // Load activities after authentication

					String response = "Authentication successful! You can close this window.";
					exchange.sendResponseHeaders(200, response.length());
					exchange.getResponseBody().write(response.getBytes());
					exchange.getResponseBody().close();
					server.stop(1);
				}
			});
			server.start(); // ✅ Ensure server is started before opening the browser
			System.out.println("🌍 Listening for Strava redirect on http://localhost:" + port);

			// Open the Strava authentication page
			String authUrl = StravaAuthService.getAuthUrl();
			java.awt.Desktop.getDesktop().browse(new URI(authUrl));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	private String extractCode(String url) {
		try {
			String[] parts = url.split("code=");
			if (parts.length > 1) {
				return parts[1].split("&")[0]; // Extracts the OAuth code
			}
		} catch (Exception e) {
			System.out.println("❌ Failed to extract code from URL: " + url);
			e.printStackTrace();
		}
		return null;
	}


	private String accessToken = "1cab3f5c4cf1128bec30eb3aefc21c627158379e";  // Retrieve from OAuth process

	public void initialize() {
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
		distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));

		loadActivities();
	}

	private void loadActivities() {
		if (accessToken == null || accessToken.isEmpty()) {
			System.out.println("❌ No access token available!");
			return;
		}

		try {
			System.out.println("🔄 Fetching activities from Strava...");
			List<Activity> activities = StravaFitnessService.getActivities(accessToken);

			if (activities == null || activities.isEmpty()) {
				System.out.println("❌ No activities received.");
				return;
			}

			tableView.setItems(FXCollections.observableArrayList(activities));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@FXML
	public void switchToWaG(ActionEvent event) throws IOException {
		TabSwitch.switchTab(paneContainer, VIEW_PATH + "WorkoutDTO-and-Goals.fxml",
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
