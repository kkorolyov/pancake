package dev.kkorolyov.pancake.skillet.ui.attribute.strategy;

import dev.kkorolyov.pancake.muffin.data.type.Attribute;
import dev.kkorolyov.pancake.skillet.ui.attribute.AttributePanel;
import dev.kkorolyov.pancake.skillet.ui.attribute.ValueDisplayer;

import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import java.util.Map;

/**
 * Displays attributes with a map value.
 */
public class MapDisplayer extends ValueDisplayer {
	@Override
	public Node display(Attribute attribute) {
		Map<String, Object> map = attribute.getValue(Map.class);

		return tooltip("Map",
				new TitledPane(
						attribute.getName(),
						new VBox(map.entrySet().stream()
								.map(entry -> new AttributePanel(
										new Attribute(entry.getKey(), entry.getValue())
												.register((inner, event) -> entry.setValue(inner.getValue())))
										.getRoot())
								.toArray(Node[]::new))));
	}

	@Override
	public boolean accepts(Class<?> c) {
		return Map.class.isAssignableFrom(c);
	}
}
