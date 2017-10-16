package dev.kkorolyov.pancake.platform.action;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.Entity;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * An action implementable using a lambda.
 */
public class FreeFormAction extends Action {
	private final Consumer<Entity> consumer;

	/**
	 * @see #FreeFormAction(Consumer, Iterable)
	 */
	@SafeVarargs
	public FreeFormAction(Consumer<Entity> consumer, Class<? extends Component>... componentTypes) {
		this(consumer, Arrays.asList(componentTypes));
	}
	/**
	 * Constructs a new free-form action
	 * @param consumer consumer invoked on accepted entities
	 * @param componentTypes minimum component types required by {@code consumer}
	 */
	public FreeFormAction(Consumer<Entity> consumer, Iterable<Class<? extends Component>> componentTypes) {
		super(componentTypes);

		this.consumer = consumer;
	}

	@Override
	protected void apply(Entity entity) {
		consumer.accept(entity);
	}
}
