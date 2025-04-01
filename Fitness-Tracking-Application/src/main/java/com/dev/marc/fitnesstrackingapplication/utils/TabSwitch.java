package com.dev.marc.fitnesstrackingapplication.utils;

import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class TabSwitch extends Transtion{

	public static void switchTab(AnchorPane paneContainer, String fxmlPath,
								 double width, double height,
								 boolean withAnimation) throws IOException {
		Parent newRoot = new TabSwitch().loadFXML(fxmlPath);

		if (withAnimation) {
			Rectangle overlay = new TabSwitch().createOverlay(width, height);
			overlay.setOpacity(0);
			Pane transitionPane = new Pane(newRoot, overlay);
			paneContainer.getChildren().setAll(transitionPane);
			new TabSwitch().applyAnimation(transitionPane, newRoot, overlay);
		} else {
			paneContainer.getChildren().setAll(newRoot);
		}
	}
}