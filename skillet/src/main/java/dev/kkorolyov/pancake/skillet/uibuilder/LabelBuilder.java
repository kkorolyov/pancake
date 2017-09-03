package dev.kkorolyov.pancake.skillet.uibuilder;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

/**
 * Convenience methods for building JavaFX Labels.
 */
public final class LabelBuilder {
	private LabelBuilder() {}

	public static Label buildLabel(String title) {
		return buildLabel(title, null);
	}
	public static Label buildLabel(String title, String tooltip) {
		Label label = new Label(title);
		if (tooltip != null) label.setTooltip(new Tooltip(tooltip));

		return label;
	}
}
