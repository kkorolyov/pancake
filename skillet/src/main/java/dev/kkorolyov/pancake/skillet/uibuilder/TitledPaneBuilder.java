package dev.kkorolyov.pancake.skillet.uibuilder;

import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;

/**
 * Convenience methods for building JavaFX TitledPanes
 */
public final class TitledPaneBuilder {
	private TitledPaneBuilder() {}

	public static TitledPane buildTitledPane(String title, Node content) {
		return buildTitledPane(title, null, content);
	}
	public static TitledPane buildTitledPane(String title, String tooltip, Node content) {
		TitledPane titledPane = new TitledPane(title, content);
		if (tooltip != null) titledPane.setTooltip(new Tooltip(tooltip));

		return titledPane;
	}
}
