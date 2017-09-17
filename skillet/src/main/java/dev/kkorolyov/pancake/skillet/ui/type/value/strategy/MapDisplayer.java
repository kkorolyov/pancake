package dev.kkorolyov.pancake.skillet.ui.type.value.strategy;

import dev.kkorolyov.pancake.muffin.data.type.Attribute;
import dev.kkorolyov.pancake.skillet.ui.type.AttributePanel;
import dev.kkorolyov.pancake.skillet.ui.type.value.ValueDisplayer;

import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import java.util.Map;

import static dev.kkorolyov.pancake.skillet.utility.ui.DisplayTransformer.paneCollector;

/**
 * Displays attributes with a map value.
 */
public class MapDisplayer extends ValueDisplayer {
	@Override
	public Node display(Attribute attribute) {
		Map<String, Object> map = attribute.getValue(Map.class);

		return tooltip("Map",
				new TitledPane(attribute.getName(), map.entrySet().stream()
						.map(entry -> new AttributePanel(
								new Attribute(entry.getKey(), entry.getValue())
										.register((inner, event) -> entry.setValue(inner.getValue()))
						)).collect(paneCollector(VBox::new))));
	}

	@Override
	public boolean accepts(Class<?> c) {
		return Map.class.isAssignableFrom(c);
	}
}
