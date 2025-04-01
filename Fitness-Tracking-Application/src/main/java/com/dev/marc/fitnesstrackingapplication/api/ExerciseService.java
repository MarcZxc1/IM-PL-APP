package com.dev.marc.fitnesstrackingapplication.api;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

public class ExerciseService {
	private static final String API_URL = "https://api.api-ninjas.com/v1/exercises";
	private static final String API_KEY = "sWUm3bS6h0MHFVkWiNQbdA==WIcUHNQW0rUIfAnD"; // Replace with your API key

	// Map of common exercises to their respective images (Modify as needed)
	private static final Map<String, String> EXERCISE_IMAGE_MAP = new HashMap<>() {{
		put("bicep curl", "https://example.com/bicep_curl.jpg");
		put("bench press", "https://example.com/bench_press.jpg");
		put("squat", "https://example.com/squat.jpg");
		put("deadlift", "https://example.com/deadlift.jpg");
		put("pull-up", "https://example.com/pullup.jpg");
	}};

	// Exercise class to hold details
	public static class Exercise {
		private final String name;
		private final String muscle;
		private final String equipment;
		private final String difficulty;
		private final String instructions;
		private final String imageUrl;

		public Exercise(String name, String muscle, String equipment, String difficulty, String instructions) {
			this.name = name;
			this.muscle = muscle;
			this.equipment = equipment;
			this.difficulty = difficulty;
			this.instructions = instructions;
			this.imageUrl = EXERCISE_IMAGE_MAP.getOrDefault(name.toLowerCase(), "https://via.placeholder.com/150");
		}

		// Getters
		public String getName() { return name; }
		public String getMuscle() { return muscle; }
		public String getEquipment() { return equipment; }
		public String getDifficulty() { return difficulty; }
		public String getInstructions() { return instructions; }
		public String getImageUrl() { return imageUrl; }

		@Override
		public String toString() {
			return "**" + name + "**\n" +
					"🦾 Muscle Group: " + muscle + "\n" +
					"🏋 Equipment: " + (equipment.isEmpty() ? "None" : equipment) + "\n" +
					"🔥 Difficulty: " + difficulty + "\n" +
					"📖 Instructions: " + instructions + "\n";
		}
	}

	// Fetch exercises from API
	public static List<Exercise> getExercises(String muscleGroup) {
		List<Exercise> exerciseList = new ArrayList<>();
		String url = API_URL + "?muscle=" + muscleGroup;

		try (CloseableHttpClient client = HttpClients.createDefault()) {
			HttpGet request = new HttpGet(url);
			request.addHeader("X-Api-Key", API_KEY);

			try (CloseableHttpResponse response = client.execute(request)) {
				String jsonResponse = EntityUtils.toString(response.getEntity());
				JSONArray exercises = new JSONArray(jsonResponse);

				if (exercises.length() == 0) {
					exerciseList.add(new Exercise("No exercises found", muscleGroup, "None", "N/A", "N/A"));
				} else {
					for (int i = 0; i < exercises.length(); i++) {
						JSONObject exercise = exercises.getJSONObject(i);
						exerciseList.add(new Exercise(
								exercise.optString("name", "Unknown"),
								exercise.optString("muscle", "Unknown"),
								exercise.optString("equipment", "None"),
								exercise.optString("difficulty", "Unknown"),
								exercise.optString("instructions", "No instructions available.")
						));
					}
				}
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		return exerciseList;
	}
}
