package dev.kkorolyov.pancake.skillet.uibuilder;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

/**
 * Convenience methods for building JavaFX Buttons.
 */
public final class ButtonBuilder {
	private ButtonBuilder() {}

	public static Button buildButton(String title, EventHandler<ActionEvent> handler) {
		return buildButton(title, null, handler);
	}
	public static Button buildButton(String title, String tooltip, EventHandler<ActionEvent> handler) {
		Button button = new Button(title);
		if (tooltip != null) button.setTooltip(new Tooltip(tooltip));
		button.setOnAction(handler);

		return button;
	}
}
