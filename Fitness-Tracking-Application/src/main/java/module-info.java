module com.dev.marc.fitnesstrackingapplication {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.web;

	requires org.controlsfx.controls;
	requires org.kordamp.ikonli.javafx;
	requires org.kordamp.bootstrapfx.core;
	requires com.dlsc.formsfx;
	requires eu.hansolo.tilesfx;
	requires com.almasb.fxgl.all;

	requires google.api.client;
	requires com.google.api.client;
	requires com.google.api.client.json.jackson2;
	requires com.google.api.client.json.gson;
	requires jdk.httpserver;
	requires com.google.api.client.extensions.jetty.auth;
	requires com.google.api.client.auth;
	requires com.google.api.client.extensions.java6.auth;
	requires org.json;
	requires org.apache.httpcomponents.client5.httpclient5;
	requires org.apache.httpcomponents.core5.httpcore5;
	requires google.api.services.fitness.v1.rev127;
	requires opencensus.api;
	requires java.net.http;
	requires org.mongodb.driver.sync.client;
	requires org.mongodb.bson;
	requires org.mongodb.driver.core;
	requires bcrypt;
	requires spring.security.crypto;
	requires spring.web;
	requires com.fasterxml.jackson.databind;
	requires java.desktop;
	requires com.google.gson;
	requires jdk.jsobject;
	requires spring.context;
	requires spring.beans;
	requires static lombok;
	requires spring.data.mongodb;
	requires spring.data.commons;
	// ✅ Add this for JSON parsing

	opens com.dev.marc.fitnesstrackingapplication.model to com.fasterxml.jackson.databind; // ✅ Allow Jackson access
	opens com.dev.marc.fitnesstrackingapplication to javafx.fxml;
	exports com.dev.marc.fitnesstrackingapplication;
	exports com.dev.marc.fitnesstrackingapplication.controller;
	opens com.dev.marc.fitnesstrackingapplication.controller to javafx.fxml;
}
