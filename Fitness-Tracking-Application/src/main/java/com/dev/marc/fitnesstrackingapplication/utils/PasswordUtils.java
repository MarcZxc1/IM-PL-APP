package com.dev.marc.fitnesstrackingapplication.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {
	private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	public static String hashPassword(String password) {
		return encoder.encode(password);
	}

	public static boolean verifyPassword(String rawPassword, String hashedPassword) {
		if (rawPassword == null || hashedPassword == null) {
			System.err.println("❌ Error: One of the passwords is null!");
			return false;
		}

		if (!hashedPassword.startsWith("$2a$")) {
			System.err.println("❌ Error: Stored password is not a valid BCrypt hash!");
			return false;
		}

		boolean matches = encoder.matches(rawPassword, hashedPassword);
		System.out.println("🔍 BCrypt Match Result: " + matches);
		return matches;
	}
}
