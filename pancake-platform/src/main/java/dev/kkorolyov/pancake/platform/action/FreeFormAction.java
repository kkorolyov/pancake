package dev.kkorolyov.pancake.platform.action;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.EntityPool;

import java.util.Arrays;
import java.util.function.BiConsumer;

/**
 * An action implementable using a lambda.
 */
public class FreeFormAction extends Action {
	private final BiConsumer<Integer, EntityPool> consumer;

	/**
	 * @see #FreeFormAction(BiConsumer, Iterable)
	 */
	@SafeVarargs
	public FreeFormAction(BiConsumer<Integer, EntityPool> consumer, Class<? extends Component>... componentTypes) {
		this(consumer, Arrays.asList(componentTypes));
	}
	/**
	 * Constructs a new free-form action
	 * @param consumer consumer invoked on accepted entities
	 * @param componentTypes minimum component types required by {@code consumer}
	 */
	public FreeFormAction(BiConsumer<Integer, EntityPool> consumer, Iterable<Class<? extends Component>> componentTypes) {
		super(componentTypes);

		this.consumer = consumer;
	}

	@Override
	protected void apply(int id, EntityPool entities) {
		consumer.accept(id, entities);
	}
}
