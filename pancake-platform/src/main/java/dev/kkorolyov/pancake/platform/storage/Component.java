package dev.kkorolyov.pancake.platform.storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A container of attributes.
 */
public class Component extends Storable<Component> implements Iterable<Attribute> {
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

	@Override
	public Iterator<Attribute> iterator() {
		return getAttributes().iterator();
	}

	/**
	 * A change to a component.
	 */
	public enum ComponentChangeEvent implements StorableChangeEvent {
		ADD,
		REMOVE
	}
}
