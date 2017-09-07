package dev.kkorolyov.pancake.skillet.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A container of attributes.
 */
public class Component extends DataObservable<Component> implements Serializable {
	private static final long serialVersionUID = -5144869208259619358L;

	private final String name;
	private final List<Attribute> attributes = new ArrayList<>();

	/**
	 * Constructs a new component.
	 * @param name component name
	 */
	public Component(String name) {
		this.name = name;
	}

	/**
	 * @param attribute added attribute
	 * @return {@code this}
	 */
	public Component addAttribute(Attribute attribute) {
		attributes.add(attribute);
		changed(ComponentChangeEvent.ADD);

		return this;
	}

	/** @return component name */
	public String getName() {
		return name;
	}

	/** @return component attributes */
	public Collection<Attribute> getAttributes() {
		return attributes;
	}

	public enum ComponentChangeEvent implements DataChangeEvent {
		ADD,
		REMOVE
	}
}
