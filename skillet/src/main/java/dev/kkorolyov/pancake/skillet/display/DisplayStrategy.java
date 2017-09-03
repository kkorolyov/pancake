package dev.kkorolyov.pancake.skillet.display;

import dev.kkorolyov.pancake.skillet.display.data.Attribute;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import java.util.HashMap;
import java.util.Map;

import static dev.kkorolyov.pancake.skillet.uibuilder.DisplayableTransformer.paneCollector;
import static dev.kkorolyov.pancake.skillet.uibuilder.LabelBuilder.buildLabel;
import static dev.kkorolyov.pancake.skillet.uibuilder.TitledPaneBuilder.buildTitledPane;

/**
 * Converts an attribute to a displayable node.
 */
public abstract class DisplayStrategy {
	private static final Map<Class<?>, DisplayStrategy> strategies = new HashMap<>();

	static {
		strategies.put(Number.class, new NumberDisplayStrategy());
		strategies.put(String.class, new TextDisplayStrategy());
		strategies.put(Map.class, new MapDisplayStrategy());
	}

	private static Node simpleDisplay(String name, Node value, String tooltip) {
		Label label = buildLabel(name + ": ", tooltip);

		BorderPane pane = new BorderPane();
		pane.setLeft(label);
		pane.setRight(value);
		BorderPane.setAlignment(label, Pos.CENTER);
		BorderPane.setAlignment(value, Pos.CENTER);

		return pane;
	}

	private static String getTooltip(String type) {
		return type + " attribute";
	}

	/**
	 * @param c type needing display
	 * @return appropriate display strategy for type {@code c}
	 * @throws UnsupportedOperationException if no strategy exists for {@code c}
	 */
	public static DisplayStrategy getStrategy(Class<?> c) {
		return strategies.entrySet().stream()
				.filter(e -> e.getKey().isAssignableFrom(c))
				.findFirst()
				.orElseThrow(() -> new UnsupportedOperationException("No display strategy for: " + c))
				.getValue();
	}

	/**
	 * Displays an attribute.
	 * @param attribute displayed attribute
	 * @return Node representation of {@code attribute}
	 */
	public abstract Node display(Attribute attribute);

	private static class NumberDisplayStrategy extends DisplayStrategy {
		@Override
		public Node display(Attribute attribute) {
			TextField numText = new TextField(attribute.getValue().toString());

			numText.textProperty().addListener((ObservableValue<? extends String> observableValue, String oldValue, String newValue) -> {
				if (!newValue.matches("[+-]?(\\d*\\.\\d*|\\d+)?")) numText.setText(oldValue);
			});
			return simpleDisplay(attribute.getName(), numText, getTooltip("Number"));
		}
	}

	private static class TextDisplayStrategy extends DisplayStrategy {
		@Override
		public Node display(Attribute attribute) {
			return simpleDisplay(attribute.getName(), new TextField(attribute.getValue().toString()), getTooltip("Text"));
		}
	}

	private static class MapDisplayStrategy extends DisplayStrategy {
		@Override
		public Node display(Attribute attribute) {
			Map<String, Object> map = attribute.getValue(Map.class);

			return buildTitledPane(attribute.getName(), getTooltip("Map"), map.entrySet().stream()
					.map(entry -> new Attribute(entry.getKey(), entry.getValue()))
					.collect(paneCollector(VBox.class)));
		}
	}
}
