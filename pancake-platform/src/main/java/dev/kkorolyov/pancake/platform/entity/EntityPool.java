package dev.kkorolyov.pancake.platform.entity;

import dev.kkorolyov.pancake.platform.action.Action;

import java.util.Comparator;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A set of uniquely-identified "component-bag" entities.
 */
public interface EntityPool {
	/** @return whether entity with ID {@code id} masks {@code signature} */
	boolean contains(UUID id, Signature signature);

	/**
	 * @param id entity ID
	 * @return stream over all components of entity with ID {@code id}
	 */
	Stream<Component> get(UUID id);

	/**
	 * Retrieves a component for a particular entity.
	 * @param id entity ID
	 * @param type type of component
	 * @return component of type {@code type} for entity with ID {@code id}, or {@code null} if does not exist
	 */
	<T extends Component> T get(UUID id, Class<T> type);
	/**
	 * Invokes a function on a component if it exists.
	 * @param id entity ID
	 * @param type component type
	 * @param function function to invoke
	 * @param <T> component type
	 * @param <R> return type
	 * @return function return value, or {@code null} if a component of type {@code type} does not exist for entity with ID {@code id}
	 */
	<T extends Component, R> R get(UUID id, Class<T> type, Function<T, R> function);

	/**
	 * Returns a stream over all entities masking a signature.
	 * @param signature signature defining a set of component types
	 * @param comparator comparator defining entity order, {@code null} results in no sorting
	 * @return all entities with a signature subset matching {@code signature}
	 */
	Stream<UUID> get(Signature signature, Comparator<UUID> comparator);

	/**
	 * Adds an action to an entity to be applied at the beginning of the next tick.
	 * @param id ID of entity to add action to
	 * @param action action to add
	 */
	void add(UUID id, Action action);
}
