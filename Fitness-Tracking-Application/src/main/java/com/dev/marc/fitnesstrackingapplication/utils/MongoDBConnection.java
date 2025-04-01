package com.dev.marc.fitnesstrackingapplication.utils;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
	private static final String CONNECTION_STRING = "mongodb://localhost:27017"; // Update if needed
	private static final String DATABASE_NAME = "FitnessTracker"; // Change to your database name

	private static MongoDatabase database;

	public static MongoDatabase getDatabase() {
		if (database == null) {
			MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
			database = mongoClient.getDatabase(DATABASE_NAME);
			System.out.println("✅ Connected to MongoDB!");
		}
		return database;
	}
}
