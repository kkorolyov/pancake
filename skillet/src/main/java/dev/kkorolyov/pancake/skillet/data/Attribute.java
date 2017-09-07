package dev.kkorolyov.pancake.skillet.data;

import java.io.Serializable;

/**
 * A field with a value.
 */
public class Attribute extends DataObservable<Attribute> implements Serializable {
	private static final long serialVersionUID = -334204822782020299L;

	private final String name;
	private Object value;

	/**
	 * Constructs a new attribute.
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
		changed(AttributeChangeEvent.VALUE);

		return oldValue;
	}

	public enum AttributeChangeEvent implements DataChangeEvent {
		VALUE
	}
}
