package dev.kkorolyov.pancake.platform.entity;

import java.util.stream.Stream;

/**
 * A container of {@link Component}s of distinct implementation types.
 */
public interface Entity extends Iterable<Component>, Comparable<Entity> {
	/**
	 * @param c class of component to get
	 * @param <T> component type
	 * @return the {@code c} component of this entity, or {@code null} if no such component
	 */
	<T extends Component> T get(Class<T> c);

	/** @return stream over all components */
	Stream<Component> stream();

	/** @return unique ID of this entity */
	int getId();
}
