package dev.kkorolyov.pancake.skillet.ui.attribute;

import dev.kkorolyov.pancake.skillet.ui.Panel;
import dev.kkorolyov.pancake.storage.Attribute;
import dev.kkorolyov.pancake.storage.Attribute.AttributeChangeEvent;
import dev.kkorolyov.pancake.storage.Storable.StorableChangeEvent;
import dev.kkorolyov.pancake.storage.StorableListener;

import javafx.scene.Node;

/**
 * Displays an {@link Attribute}.
 */
public class AttributePanel implements Panel, StorableListener<Attribute> {
	private Node root;

	/**
	 * Constructs a new attribute display.
	 * @param attribute displayed attribute
	 */
	public AttributePanel(Attribute attribute) {
		changed(attribute, AttributeChangeEvent.VALUE);

		attribute.register(this);
	}

	@Override
	public Node getRoot() {
		return root;
	}

	@Override
	public void changed(Attribute target, StorableChangeEvent event) {
		if (AttributeChangeEvent.VALUE == event) {
			root = ValueDisplayers.getStrategy(target).display(target);
			root.setId(target.getName());
		}
	}
}
