package com.dev.marc.fitnesstrackingapplication.model;

// This class provides methods to interact with the database for user-related operations.
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UserService {
	public static String fetchUserInfo() {
		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder()
					.uri(new URI("http://localhost:8080/user"))
					.GET()
					.build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			return response.body(); // Returns JSON with user info
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
