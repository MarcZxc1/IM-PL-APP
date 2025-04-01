package com.dev.marc.fitnesstrackingapplication.model;

import org.bson.Document;

public class GoogleUser {
	private String email;
	private String password; // ✅ Match MongoDB field name

	public GoogleUser(String email, String password) { // ✅ Update constructor
		this.email = email;
		this.password = password;
	}

	public String getEmail() { return email; }

	public String getPassword() { return password; } // ✅ Update getter method

	// ✅ Fix: Ensure correct field mapping from MongoDB
	public static GoogleUser fromDocument(Document doc) {
		String storedPassword = doc.getString("password");

		if (storedPassword == null) {
			System.err.println("❌ Error: Password field is missing in MongoDB for " + doc.getString("email"));
		}

		return new GoogleUser(doc.getString("email"), storedPassword);
	}

}
