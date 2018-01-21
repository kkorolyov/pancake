package dev.kkorolyov.pancake.platform.entity;

import dev.kkorolyov.pancake.platform.action.Action;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A set of uniquely-identified "component-bag" entities.
 */
public interface EntityPool {
	/** @return whether entity with ID {@code id} masks {@code signature} */
	boolean contains(int id, Signature signature);

	/**
	 * @param id entity ID
	 * @return stream over all components of entity with ID {@code id}
	 */
	Stream<Component> get(int id);

	/**
	 * Retrieves a component for a particular entity.
	 * @param id entity IDw
	 * @param type type of component
	 * @return component of type {@code type} for entity with ID {@code id}, or {@code null} if does not exist
	 */
	<T extends Component> T get(int id, Class<T> type);
	/**
	 * Invokes a function on a component if it exists.
	 * @param id entity ID
	 * @param type component type
	 * @param function function to invoke
	 * @param <T> component type
	 * @param <R> return type
	 * @return function return value, or {@code null} if a component of type {@code type} does not exist for entity with ID {@code id}
	 */
	<T extends Component, R> R get(int id, Class<T> type, Function<T, R> function);

	/**
	 * Adds an action to an entity to be applied at the beginning of the next tick.
	 * @param id ID of entity to add action to
	 * @param action action to add
	 */
	void add(int id, Action action);
}
