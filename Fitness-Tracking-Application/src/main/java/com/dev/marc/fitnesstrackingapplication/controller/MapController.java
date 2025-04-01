package com.dev.marc.fitnesstrackingapplication.controller;

import com.dev.marc.fitnesstrackingapplication.model.WorkoutDTO;
import com.dev.marc.fitnesstrackingapplication.utils.TabSwitch;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
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

	@FXML private WebView mapWebView;
	@FXML private TextField longitudeField;
	@FXML private TextField latitudeField;
	@FXML private TextField distanceField;
	@FXML private TextArea resultArea;
	@FXML private AnchorPane paneContainer;
	@FXML private Button loginButton;
	@FXML private Label stepsLabel;
	@FXML private Label heartRateLabel;

	private final RestTemplate restTemplate = new RestTemplate();
	private final String API_URL = "http://localhost:8080/api/workouts/nearby";
	private final String STRAVA_ACTIVITIES_URL = "https://www.strava.com/api/v3/athlete/activities";
	private static final String BACKEND_TOKEN_URL = "http://localhost:8080/api/strava/token";
	private WebEngine webEngine;
	private boolean isWebViewLoaded = false;
	private String accessToken = "";

	@FXML
	private void loginWithStrava() {
		openBrowser("http://localhost:8080/api/strava/login");
	}

	private void openBrowser(String url) {
		try {
			Desktop.getDesktop().browse(new URI(url));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleFetchActivities(ActionEvent event) throws IOException {
		fetchStravaActivities();
	}

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
					ObjectMapper objectMapper = new ObjectMapper();
					JsonNode activitiesNode = objectMapper.readTree(response.getBody());

					List<String> activities = new ArrayList<>();
					for (JsonNode activity : activitiesNode) {
						String activityName = activity.path("name").asText("Unknown Activity");
						String distance = activity.path("distance").asText("0");
						String movingTime = activity.path("moving_time").asText("0");
						activities.add("🏃 " + activityName + " | Distance: " + distance + "m | Time: " + movingTime + "s");
					}

					Platform.runLater(() -> resultArea.setText(String.join("\n", activities)));
				} else {
					showError("❌ Failed to fetch activities: " + response.getStatusCode());
				}
			} catch (Exception e) {
				showError("❌ Error fetching activities: " + e.getMessage());
			}
		}).start();
	}

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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
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

	/**
	 * Displays error messages.
	 */
	private void showError(String message) {
		Platform.runLater(() -> resultArea.setText("❌ " + message));
		System.err.println("❌ " + message);
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
	@FXML
	public void goToMap(ActionEvent event) throws IOException {
		TabSwitch.switchTab(paneContainer, VIEW_PATH +
				"Track.fxml", 1250, 680, false);
	}

	public void setPaneContainer(AnchorPane paneContainer) {this.paneContainer = paneContainer;}

	@FXML
	public void homeButton(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(VIEW_PATH+ "Dashboard.fxml"));
		Parent root = loader.load();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(new Scene(root, 1250, 680));
		stage.show();


	}
}
