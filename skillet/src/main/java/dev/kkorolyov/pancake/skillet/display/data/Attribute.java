package dev.kkorolyov.pancake.skillet.display.data;

import dev.kkorolyov.pancake.skillet.display.DisplayStrategy;
import dev.kkorolyov.pancake.skillet.display.Displayable;

import javafx.scene.Node;

/**
 * A field with a value.
 */
public class Attribute implements Displayable {
	private final String name;
	private Object value;

	/**
	 * Constructs a new attribute with an initial value.
	 * @param name attribute name
	 * @param value attribute value
	 */
	public Attribute(String name, Object value) {
		this.name = name;
		setValue(value);
	}

	/** @return attribute name */
	public String getName() {
		return name;
	}

	/** @return attribute value */
	public Object getValue() {
		return value;
	}
	/**
	 * @param c class to cast to
	 * @return attribute value as type {@code c}
	 */
	public <T> T getValue(Class<T> c) {
		return c.cast(value);
	}

	/**
	 * @param value new attribute value
	 * @return previous attribute value
	 */
	public Object setValue(Object value) {
		Object oldValue = this.value;

		this.value = value;

		return oldValue;
	}

	@Override
	public Node toNode() {
		return DisplayStrategy.getStrategy(value.getClass())
				.display(this);
	}
}
