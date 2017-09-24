package dev.kkorolyov.pancake.skillet.ui.attribute.strategy;

import dev.kkorolyov.pancake.platform.storage.Attribute;
import dev.kkorolyov.pancake.skillet.ui.attribute.AttributePanel;
import dev.kkorolyov.pancake.skillet.ui.attribute.ValueDisplayer;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import java.util.Map;

import static dev.kkorolyov.pancake.skillet.UIDecorator.decorate;

/**
 * Displays attributes with a map value.
 */
public class MapDisplayer extends ValueDisplayer {
	@Override
	public Node display(Attribute attribute) {
		Map<String, Object> map = attribute.getValue(Map.class);

		return decorate(new TitledPane())
				.styleClass("attribute")
				.graphic(decorate(new Label(attribute.getName()))
						.styleClass("attribute-name")
						.tooltip(tooltip("Map"))
						.get())
				.content(decorate(new VBox(map.entrySet().stream()
						.map(entry -> new AttributePanel(
								new Attribute(entry.getKey(), entry.getValue())
										.register((inner, event) -> entry.setValue(inner.getValue())))
								.getRoot())
						.toArray(Node[]::new)))
						.styleClass("attribute-content")
						.get())
				.get();
	}

	@Override
	public boolean accepts(Class<?> c) {
		return Map.class.isAssignableFrom(c);
	}
}
