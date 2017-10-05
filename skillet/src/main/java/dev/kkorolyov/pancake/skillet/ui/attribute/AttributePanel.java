package dev.kkorolyov.pancake.skillet.ui.attribute;

import dev.kkorolyov.pancake.platform.storage.Attribute;
import dev.kkorolyov.pancake.skillet.ui.Panel;

import javafx.scene.Node;

/**
 * Displays an {@link Attribute}.
 */
public class AttributePanel implements Panel {
	private Node root;

	/**
	 * Constructs a new attribute display.
	 * @param attribute displayed attribute
	 */
	public AttributePanel(Attribute attribute) {
		root = ValueDisplayers.getStrategy(attribute).display(attribute);
		root.setId(attribute.getName());
	}

	@Override
	public Node getRoot() {
		return root;
	}
}
