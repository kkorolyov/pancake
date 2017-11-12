package dev.kkorolyov.pancake.skillet.model;

import dev.kkorolyov.pancake.platform.entity.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Contains an attribute map.
 */
public class GenericComponent extends Model<GenericComponent> implements Component, Iterable<Entry<String, Object>> {
	private final String name;
	private final Map<String, Object> attributes;

	/**
	 * Constructs a new component.
	 * @param name component name
	 * @param attributes component attributes
	 */
	public GenericComponent(String name, Map<String, Object> attributes) {
		this.name = name;
		this.attributes = attributes;
	}

	/** @return component name */
	public String getName() {
		return name;
	}

	/** @return component attributes */
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	/** @return	attribute entries as a stream */
	public Stream<Entry<String, Object>> stream() {
		return attributes.entrySet().stream();
	}

	@Override
	public Iterator<Entry<String, Object>> iterator() {
		return attributes.entrySet().iterator();
	}

	/** @return deep copy of {@code this} */
	public GenericComponent copy() {
		return new GenericComponent(name, new HashMap<>(attributes));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || !getClass().isAssignableFrom(obj.getClass())) return false;

		GenericComponent o = (GenericComponent) obj;
		return Objects.equals(name, o.name)
				&& Objects.equals(attributes, o.attributes);
	}
	@Override
	public int hashCode() {
		return Objects.hash(name, attributes);
	}
}
