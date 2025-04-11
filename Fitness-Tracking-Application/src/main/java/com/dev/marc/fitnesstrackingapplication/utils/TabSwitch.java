package com.dev.marc.fitnesstrackingapplication.utils;

import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class TabSwitch extends Transtion{

	public static void switchTab(Pane paneContainer, String fxmlPath,
								 double width, double height,
								 boolean withAnimation) throws IOException {
		Parent newRoot = new TabSwitch().loadFXML(fxmlPath);  // Ensure this is called on an instance

		if (withAnimation) {
			Rectangle overlay = new TabSwitch().createOverlay(width, height);  // Ensure this is called on an instance
			overlay.setOpacity(0);
			Pane transitionPane = new Pane(newRoot, overlay);
			paneContainer.getChildren().setAll(transitionPane);
			new TabSwitch().applyAnimation(transitionPane, newRoot, overlay);  // Ensure this is called on an instance
		} else {
			paneContainer.getChildren().setAll(newRoot);
		}
	}
}