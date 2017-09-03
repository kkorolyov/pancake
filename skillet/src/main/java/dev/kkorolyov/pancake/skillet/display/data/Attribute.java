package dev.kkorolyov.pancake.skillet.display.data;

import dev.kkorolyov.pancake.skillet.display.DisplayStrategy;
import dev.kkorolyov.pancake.skillet.display.Displayable;

import javafx.scene.Node;
import java.util.function.BiConsumer;

/**
 * A field with a value.
 */
public class Attribute implements Displayable {
	private final String name;
	private Object value;
	private final BiConsumer<Object, Object> valueChangedListener;

	/**
	 * Constructs a new attribute.
	 * @param name attribute name
	 * @param value attribute value
	 */
	public Attribute(String name, Object value) {
		this(name, value, null);
	}
	/**
	 * Constructs a new attribute.
	 * @param name attribute name
	 * @param value attribute value
	 * @param valueChangedListener listener called with (oldValue, newValue) when this attribute's value changes
	 */
	public Attribute(String name, Object value, BiConsumer<Object, Object> valueChangedListener) {
		this.name = name;
		setValue(value);
		this.valueChangedListener = valueChangedListener;
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

		if (valueChangedListener != null) {
			valueChangedListener.accept(oldValue, this.value);
		}
		return oldValue;
	}

	@Override
	public Node toNode() {
		return DisplayStrategy.getStrategy(value.getClass())
				.display(this);
	}
}
