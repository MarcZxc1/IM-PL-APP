package com.dev.marc.fitnesstrackingapplication.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true) // ✅ Ignores unknown JSON fields
public class WorkoutDTO {
	private String _id;
	private String userId;
	private String workoutType;
	private double duration;
	private long timestamp;
	private Location location;
	private String name;
	private String description;

	// ✅ Default constructor (important for Jackson)
	public WorkoutDTO() {}

	// ✅ Constructor for full initialization
	@JsonCreator
	public WorkoutDTO(
			@JsonProperty("_id") String _id,
			@JsonProperty("userId") String userId,
			@JsonProperty("workoutType") String workoutType,
			@JsonProperty("duration") double duration,
			@JsonProperty("timestamp") long timestamp,
			@JsonProperty("location") Location location,
			@JsonProperty("name") String name,
			@JsonProperty("description") String description
	) {
		this._id = _id;
		this.userId = userId;
		this.workoutType = workoutType;
		this.duration = duration;
		this.timestamp = timestamp;
		this.location = location;
		this.name = name;
		this.description = description;
	}

	// ✅ Getters (required for Jackson to read fields)
	public String getId() { return _id; }
	public String getUserId() { return userId; }
	public String getWorkoutType() { return workoutType; }
	public double getDuration() { return duration; }
	public long getTimestamp() { return timestamp; }
	public Location getLocation() { return location; }
	public String getName() { return name; }
	public String getDescription() { return description; }

	// ✅ Setters (optional but good for mutability)
	public void setId(String _id) { this._id = _id; }
	public void setUserId(String userId) { this.userId = userId; }
	public void setWorkoutType(String workoutType) { this.workoutType = workoutType; }
	public void setDuration(double duration) { this.duration = duration; }
	public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
	public void setLocation(Location location) { this.location = location; }
	public void setName(String name) { this.name = name; }
	public void setDescription(String description) { this.description = description; }
}
