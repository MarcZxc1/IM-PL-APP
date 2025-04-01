package com.dev.marc.fitnesstrackingapplication.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.dev.marc.fitnesstrackingapplication.model.GoogleUser;
import org.bson.Document;

public class GoogleUserRepository {
	private static final String COLLECTION_NAME = "google_users"; // ✅ Ensure this matches your MongoDB collection
	private final MongoCollection<Document> userCollection;

	public GoogleUserRepository() {
		MongoDatabase database = com.dev.marc.fitnesstrackingapplication.utils.MongoDBConnection.getDatabase();
		this.userCollection = database.getCollection(COLLECTION_NAME);
	}

	public GoogleUser findByEmail(String email) {
		System.out.println("🔍 Searching for user: " + email);
		Document doc = userCollection.find(Filters.eq("email", email)).first();

		if (doc == null) {
			System.out.println("❌ User not found in MongoDB!");
			return null;
		}

		System.out.println("✅ User found in MongoDB: " + doc.toJson());
		return GoogleUser.fromDocument(doc);
	}

}
