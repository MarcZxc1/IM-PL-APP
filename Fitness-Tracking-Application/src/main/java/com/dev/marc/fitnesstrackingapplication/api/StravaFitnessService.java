package com.dev.marc.fitnesstrackingapplication.api;

import com.dev.marc.fitnesstrackingapplication.model.Activity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StravaFitnessService {
	public static List<Activity> getActivities(String accessToken) throws IOException {
		URL url = new URL("https://www.strava.com/api/v3/athlete/activities");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Authorization", "Bearer " + accessToken);

		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		StringBuilder response = new StringBuilder();
		String line;

		while ((line = br.readLine()) != null) {
			response.append(line);
		}
		br.close();

		// Convert JSON response to List of Activity objects
		List<Activity> activities = new ArrayList<>();
		JSONArray jsonArray = new JSONArray(response.toString());

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			activities.add(new Activity(
					obj.getString("name"),
					obj.getString("type"),
					obj.getDouble("distance") + " meters"
			));
		}

		return activities;
	}
}

