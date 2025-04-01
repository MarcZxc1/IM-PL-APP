//package com.dev.marc.fitnesstrackingapplication.service;
//
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//public class StravaService {
//
//	private static final String STRAVA_API_URL = "https://www.strava.com/api/v3/athlete/activities";
//
//	public String getLatestActivity(String accessToken) {
//		RestTemplate restTemplate = new RestTemplate();
//		String url = STRAVA_API_URL + "?access_token=" + accessToken;
//		return restTemplate.getForObject(url, String.class);
//	}
//}
