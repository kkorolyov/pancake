package dev.kkorolyov.pancake.skillet.ui.attribute;

import dev.kkorolyov.pancake.muffin.data.DataChangeListener;
import dev.kkorolyov.pancake.muffin.data.DataObservable.DataChangeEvent;
import dev.kkorolyov.pancake.muffin.data.type.Attribute;
import dev.kkorolyov.pancake.muffin.data.type.Attribute.AttributeChangeEvent;
import dev.kkorolyov.pancake.skillet.ui.Panel;

import javafx.scene.Node;

/**
 * Displays an {@link Attribute}.
 */
public class AttributePanel implements Panel, DataChangeListener<Attribute> {
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
	public void changed(Attribute target, DataChangeEvent event) {
		if (AttributeChangeEvent.VALUE == event) {
			root = ValueDisplayers.getStrategy(target).display(target);
			root.setId(target.getName());
		}
	}
}
