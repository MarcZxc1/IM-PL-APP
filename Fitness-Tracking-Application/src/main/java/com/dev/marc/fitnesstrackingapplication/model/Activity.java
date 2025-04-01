package com.dev.marc.fitnesstrackingapplication.model;

public class Activity {
	private String name;
	private String type;
	private double distance;

	public Activity(String name, String type, String distance) {
		this.name = name;
		this.type = type;
		this.distance = Double.parseDouble(distance);
	}

	// Getters
	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public double getDistance() {
		return distance;
	}

	// Setters (if needed)
	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
}
