package com.dev.marc.fitnesstrackingapplication.api;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class StravaAuthService {
	private static final String CLIENT_ID = "152285";
	private static final String CLIENT_SECRET = "7b411d61c8158c06ed1e71d9c02ed506d8354b0d";
	private static final String REDIRECT_URI = "http://localhost";

	public static String getAuthUrl() {
		return "https://www.strava.com/oauth/authorize?client_id=" + CLIENT_ID
				+ "&response_type=code&redirect_uri=" + REDIRECT_URI
				+ "&scope=activity:read_all";
	}

	public static String exchangeCodeForToken(String code) throws IOException, InterruptedException {
		String requestBody = "client_id=" + CLIENT_ID
				+ "&client_secret=" + CLIENT_SECRET
				+ "&code=" + code
				+ "&grant_type=authorization_code";

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://www.strava.com/oauth/token"))
				.header("Content-Type", "application/x-www-form-urlencoded")
				.POST(HttpRequest.BodyPublishers.ofString(requestBody))
				.build();

		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		JSONObject json = new JSONObject(response.body());

		return json.getString("access_token");  // ✅ Correct key extraction
	}

}
