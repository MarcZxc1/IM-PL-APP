package com.dev.marc.fitnesstrackingapplication.controller;

import com.dev.marc.fitnesstrackingapplication.model.Location;
import com.dev.marc.fitnesstrackingapplication.model.WorkoutDTO;
import com.dev.marc.fitnesstrackingapplication.utils.TabSwitch;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.List;

@Controller
public class MapController implements Initializable {

	private static final String VIEW_PATH = "/com/dev/marc/fitnesstrackingapplication/view/";
	private static final String BACKEND_TOKEN_URL = "http://localhost:8080/api/strava/token";
	private static final String STRAVA_ACTIVITIES_URL = "https://www.strava.com/api/v3/athlete/activities";
	private static final String API_URL = "http://localhost:8080/api/workouts/nearby";

	@FXML private WebView mapWebView;
	@FXML private TextField longitudeField;
	@FXML private TextField latitudeField;
	@FXML private TextField distanceField;
	@FXML private TextArea resultArea;
	@FXML private Pane paneContainer;
	@FXML private Button loginButton;
	@FXML private Label stepsLabel;
	@FXML private Label heartRateLabel;
	@FXML private TableView<WorkoutDTO> tableView;
	@FXML private TableColumn<WorkoutDTO, String> workoutNameColumn;
	@FXML private TableColumn<WorkoutDTO, String> workoutTypeColumn;
	@FXML private TableColumn<WorkoutDTO, Double> distanceColumn;
	@FXML private TableColumn<WorkoutDTO, String> locationColumn;


	private final RestTemplate restTemplate = new RestTemplate();
	private WebEngine webEngine;
	private boolean isWebViewLoaded = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupTableColumns();
		initializeMapWebView();
		populateTable(Collections.emptyList());
		setupTableViewSelectionListener();
	}

	// Setup TableView columns
	private void setupTableColumns() {
		workoutNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
		workoutTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getWorkoutType()));

		locationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation().toString()));

	}

	private void setupTableViewSelectionListener() {
		tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, selectedWorkout) -> {
			if (selectedWorkout != null) {
				// Call JavaScript function to load the track
				String workoutName = selectedWorkout.getName();
				String polyline = selectedWorkout.getPolyline();
				String js = String.format("loadStravaTracks([{ name: '%s', polyline: '%s' }])",
						escapeJS(workoutName), escapeJS(polyline));
				webEngine.executeScript(js);
			}
		});
	}



	// Initialize WebView for map
	private void initializeMapWebView() {
		webEngine = mapWebView.getEngine();
		String mapUrl = getClass().getResource("/map.html") != null ? getClass().getResource("/map.html").toExternalForm() : "";

		if (!mapUrl.isEmpty()) {
			webEngine.load(mapUrl);
			System.out.println("✅ Map loaded: " + mapUrl);
		} else {
			System.err.println("❌ map.html not found! Check path.");
		}

		webEngine.documentProperty().addListener((obs, oldDoc, newDoc) -> {
			if (newDoc != null) {
				isWebViewLoaded = true;
				System.out.println("✅ WebView loaded successfully.");
			}
		});
	}

	// Populate TableView with workouts
	@FXML
	private void populateTable(List<WorkoutDTO> workouts) {
		workoutNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
		workoutTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getWorkoutType()));
		distanceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getDuration()).asObject());
		locationColumn.setCellValueFactory(cellData -> {
			Location location = cellData.getValue().getLocation();
			return new SimpleStringProperty(location != null ? location.toString() : "Unknown");
		});

		// Set the items for the TableView
		tableView.setItems(FXCollections.observableArrayList(workouts));
	}





			// Open browser for Strava login
	@FXML
	private void loginWithStrava() {
		openBrowser("http://localhost:8080/api/strava/login");
	}

	// Open a URL in the default browser
	private void openBrowser(String url) {
		try {
			Desktop.getDesktop().browse(new URI(url));
		} catch (Exception e) {
			e.printStackTrace();
			showError("⚠️ Failed to open browser: " + e.getMessage());
		}
	}

	// Fetch Strava activities
	@FXML
	public void handleFetchActivities(ActionEvent event) {
		fetchStravaActivities();
	}

	// Handle workout selection from the result area text
	@FXML
	private void handleWorkoutSelectionFromTextArea(ActionEvent event) {
		String selectedText = resultArea.getSelectedText();

		if (selectedText != null && !selectedText.isEmpty()) {
			String workoutName = extractWorkoutName(selectedText);
			String polyline = extractPolyline(selectedText);

			if (workoutName != null && polyline != null) {
				loadPolylineToMap(workoutName, polyline);
			} else {
				showError("Error: Workout name or polyline not found.");
			}
		} else {
			showError("No workout selected from the TextArea.");
		}
	}

	// Extract workout name from the selected text
	private String extractWorkoutName(String selectedText) {
		String[] parts = selectedText.split(" \\| ");
		return parts.length > 0 ? parts[0].trim() : null;
	}

	// Extract polyline from the selected text
	private String extractPolyline(String selectedText) {
		String[] parts = selectedText.split(" ");
		return parts.length > 1 ? parts[1].trim() : null;
	}

	// Load polyline to map
	private void loadPolylineToMap(String workoutName, String polyline) {
		String js = String.format("loadStravaTracks([{ name: '%s', polyline: '%s' }])", escapeJS(workoutName), polyline);
		webEngine.executeScript(js);
	}

	// Escape special characters for JS string
	private String escapeJS(String input) {
		return input != null ? input.replace("'", "\\'") : "";
	}

	// Fetch Strava activities from the backend
	private void fetchStravaActivities() {
		new Thread(() -> {
			try {
				String accessToken = getAccessTokenFromBackend();
				if (accessToken == null) {
					showError("⚠️ No valid access token! Please log in first.");
					return;
				}

				String url = STRAVA_ACTIVITIES_URL + "?access_token=" + accessToken;
				ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

				if (response.getStatusCode() == HttpStatus.OK) {
					List<WorkoutDTO> workouts = parseStravaActivities(response.getBody());
					Platform.runLater(() -> populateTable(workouts)); // Populate TableView with workouts
				} else {
					showError("❌ Failed to fetch activities: " + response.getStatusCode());
				}
			} catch (Exception e) {
				showError("❌ Error fetching activities: " + e.getMessage());
			}
		}).start();
	}


	private List<WorkoutDTO> parseStravaActivities(String responseBody) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode activitiesNode = objectMapper.readTree(responseBody);
		List<WorkoutDTO> workouts = new ArrayList<>();
		for (JsonNode activity : activitiesNode) {
			String activityName = activity.path("name").asText("Unknown Activity");
			double distance = activity.path("distance").asDouble(0);
			// You can use the duration field or any other value to represent the workout's distance
			// Here we assume 'duration' is being used as distance for display purposes.
			String polyline = activity.path("polyline").asText("");  // Get polyline if available
			// Create a new workout, using the setters since the current constructor does not include polyline.
			WorkoutDTO workout = new WorkoutDTO();
			workout.setName(activityName);
			workout.setWorkoutType("Running"); // Hardcode if needed, or parse from response
			workout.setDuration(distance); // Using distance as duration field for now
			workout.setPolyline(polyline);
			// Location remains unset or you can assign a default if desired.
			workout.setLocation(null);
			workouts.add(workout);
		}
		return workouts;
	}


	// Get access token from backend
	private String getAccessTokenFromBackend() {
		try {
			ResponseEntity<String> response = restTemplate.getForEntity(BACKEND_TOKEN_URL, String.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode jsonNode = objectMapper.readTree(response.getBody());
				return jsonNode.path("access_token").asText(null);
			}
		} catch (Exception e) {
			System.err.println("❌ Error fetching access token: " + e.getMessage());
		}
		return null;
	}

	// Fetch nearby workouts based on location
	@FXML
	public void fetchNearbyWorkouts(ActionEvent event) {
		new Thread(() -> {
			try {
				double latitude = Double.parseDouble(latitudeField.getText().trim());
				double longitude = Double.parseDouble(longitudeField.getText().trim());
				double distanceKm = Double.parseDouble(distanceField.getText().trim());
				double radiusMeters = distanceKm * 1000;

				String requestUrl = API_URL + "?longitude=" + longitude + "&latitude=" + latitude + "&radius=" + radiusMeters;
				System.out.println("📡 Sending request: " + requestUrl);

				ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);

				if (response.getStatusCode() == HttpStatus.OK) {
					ObjectMapper objectMapper = new ObjectMapper();
					JsonNode rootNode = objectMapper.readTree(response.getBody());
					JsonNode dataNode = rootNode.path("data");

					WorkoutDTO[] workoutsArray = objectMapper.treeToValue(dataNode, WorkoutDTO[].class);
					List<WorkoutDTO> workouts = Arrays.asList(workoutsArray);

					Platform.runLater(() -> {
						try {
							String jsonOutput = objectMapper.writeValueAsString(workouts);
							resultArea.setText(jsonOutput);
							displayLocationsOnMap(workouts);
						} catch (Exception e) {
							showError("Error converting workouts to JSON: " + e.getMessage());
						}
					});
				} else {
					showError("Failed to fetch workouts: HTTP " + response.getStatusCode());
				}
			} catch (Exception e) {
				showError("Error: " + e.getMessage());
			}
		}).start();
	}

	private void displayLocationsOnMap(List<WorkoutDTO> workouts) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonLocations = objectMapper.writeValueAsString(workouts);
			Platform.runLater(() -> {
				if (isWebViewLoaded) {
					webEngine.executeScript("loadWorkouts(" + jsonLocations + ");");
				} else {
					showError("WebView not ready yet.");
				}
			});
		} catch (Exception e) {
			showError("JSON Processing Error: " + e.getMessage());
		}
	}





	// Parse nearby workouts from response JSON
	private WorkoutDTO[] parseNearbyWorkouts(String responseBody) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode = objectMapper.readTree(responseBody);
		JsonNode dataNode = rootNode.path("data");
		return objectMapper.treeToValue(dataNode, WorkoutDTO[].class);
	}

	// Display nearby workouts on the map and in result area
	private void displayNearbyWorkouts(List<WorkoutDTO> workouts) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonLocations = objectMapper.writeValueAsString(workouts);
			if (isWebViewLoaded) {
				webEngine.executeScript("loadWorkouts(" + jsonLocations + ");");
			} else {
				showError("WebView not ready yet.");
			}

			String jsonOutput = objectMapper.writeValueAsString(workouts);
			resultArea.setText(jsonOutput);
		} catch (Exception e) {
			showError("JSON Processing Error: " + e.getMessage());
		}
	}

	// Show error message in result area
	private void showError(String message) {
		Platform.runLater(() -> resultArea.setText("❌ " + message));
		System.err.println("❌ " + message);
	}

	// Navigate to map view
	@FXML
	public void goToMap(ActionEvent event) throws IOException {
		TabSwitch.switchTab(paneContainer, VIEW_PATH + "Track.fxml", 1250, 680, false);
	}

	// Navigate to home dashboard
	@FXML
	public void homeButton(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(VIEW_PATH + "Dashboard.fxml"));
		Parent root = loader.load();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(new Scene(root, 1250, 680));
		stage.show();
	}

	// Set the container for pane switching
	public void setPaneContainer(Pane paneContainer) {
		this.paneContainer = paneContainer;
	}
}
