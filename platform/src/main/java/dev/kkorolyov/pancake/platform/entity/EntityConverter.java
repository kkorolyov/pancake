package dev.kkorolyov.pancake.platform.entity;

import dev.kkorolyov.pancake.platform.registry.Registry;
import dev.kkorolyov.pancake.platform.utility.ArgVerify;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Converts between an entity's set of components and a serializable map representation of them.
 * Is provided a component registry to resolve entity component references against, to support reusing the same component instance for multiple read entities.
 */
public final class EntityConverter {
	private final Registry<? extends Component> registry;

	/**
	 * Constructs a new entity converter using {@code registry} to resolve referenced components in read entities.
	 */
	public EntityConverter(Registry<? extends Component> registry) {
		this.registry = registry;
	}

	/**
	 * Returns the components read from serializable map representation {@code data} mapped by their class.
	 */
	public Map<Class<? extends Component>, Component> read(Map<String, Object> data) {
		return data.entrySet().stream()
				// string values are always a reference
				.map(e -> e.getValue() instanceof String
						? ArgVerify.nonNull((String) e.getValue(), registry.get((String) e.getValue()))
						: ComponentConverters.get(e.getKey()).read(e.getValue())
				)
				.collect(Collectors.toMap(
						Component::getClass,
						t -> t
				));
	}

	/**
	 * Returns a serializable map representation of {@code components}.
	 */
	public Map<String, Object> write(Iterable<? extends Component> components) {
		return StreamSupport.stream(components.spliterator(), false)
				.collect(Collectors.toMap(
						t -> t.getClass().getName(),
						t -> ComponentConverters.get(t.getClass().getName()).write(t)
				));
	}
}
