package dev.kkorolyov.pancake.skillet.ui.attribute.strategy;

import dev.kkorolyov.pancake.muffin.data.type.Attribute;
import dev.kkorolyov.pancake.skillet.ui.attribute.ValueDisplayer;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import java.net.URI;

/**
 * Displays attributes with a URI value.
 */
public class URIDisplayer extends ValueDisplayer {
	@Override
	public Node display(Attribute attribute) {
		return simpleDisplay(attribute.getName(),
				"URI",
				// TODO
				new TextField(attribute.getValue().toString()));
	}

	@Override
	public boolean accepts(Class<?> c) {
		return URI.class.isAssignableFrom(c);
	}
}
