package com.dev.marc.fitnesstrackingapplication.model;

public class Location {
	private String name;
	private double latitude;
	private double longitude;
	private String type;// Example: "fitness", "gym"
	private double coordinates[];

	public Location() {} // Default constructor is required for JSON mapping

	public Location(String name, double latitude, double longitude, String type, double coordinates[]) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.type = type;
		this.coordinates = coordinates;
	}

	public Location(String workoutType, double latitude, double longitude, String workout) {
		this.name = workoutType;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	// Getters
	public String getName() { return name; }
	public double getLatitude() { return latitude; }
	public double getLongitude() { return longitude; }
	public String getType() { return type; }
	public double[] getCoordinates() { return coordinates; }

	// Setters
	public void setName(String name) { this.name = name; }
	public void setLatitude(double latitude) { this.latitude = latitude; }
	public void setLongitude(double longitude) { this.longitude = longitude; }
	public void setType(String type) { this.type = type; }
}
