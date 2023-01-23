package dev.kkorolyov.pancake.platform;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.EntityPool;
import dev.kkorolyov.pancake.platform.utility.Sampler;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * Performs work on entities matching a certain component signature.
 */
public abstract class GameSystem implements Iterable<Class<? extends Component>> {
	private final Collection<Class<? extends Component>> signature;
	private EntityPool entities;

	private final Sampler sampler = new Sampler();

	/**
	 * Constructs a new system with {@code signature} defining the minimum component set of entities processed by this system.
	 */
	@SafeVarargs
	protected GameSystem(Class<? extends Component>... signature) {
		this.signature = Arrays.stream(signature).toList();
	}

	/**
	 * Returns a system that runs {@code op} once per update.
	 * Useful for simple, pipeline-spanning hooks like setting up rendering, swapping buffers, or polling events.
	 */
	public static GameSystem hook(Runnable op) {
		// dummy component to avoid iterating over all entities
		return new GameSystem(DummyComponent.class) {
			@Override
			protected void update(Entity entity, long dt) {}

			@Override
			protected void after() {
				op.run();
			}
		};
	}

	/**
	 * Invoked on each entity affected by this system.
	 * @param entity entity to update
	 * @param dt {@code ns} elapsed since last {@code update} to this system
	 */
	protected abstract void update(Entity entity, long dt);

	/**
	 * Invoked at the beginning of an update cycle.
	 * Intended for any static pre-update logic.
	 */
	protected void before() {}
	/**
	 * Invoked at the end of an update cycle.
	 * Intended for any static post-update logic.
	 */
	protected void after() {}

	/**
	 * Returns a new entity.
	 */
	protected final Entity create() {
		return entities.create();
	}
	/**
	 * Destroys entity by {@code id}.
	 */
	protected final void destroy(int id) {
		entities.destroy(id);
	}

	/**
	 * Attaches resources to this system.
	 */
	final void attach(EntityPool entities) {
		this.entities = entities;
	}
	/**
	 * Detaches all resources from this system.
	 */
	final void detach() {
		entities = null;
	}

	/**
	 * Executes a single update on this system with {@code dt} {@code (ns)} timestep.
	 * A {@code dt < 0} is allowed (can imply e.g. update in reverse).
	 */
	final void update(long dt) {
		sampler.reset();

		before();
		for (Entity entity : entities.get(signature)) update(entity, dt);
		after();

		sampler.sample();
	}

	/**
	 * Returns this system's sampler.
	 */
	public Sampler getSampler() {
		return sampler;
	}

	/**
	 * Returns the number of component types in this system's signature.
	 */
	public final int size() {
		return signature.size();
	}

	/**
	 * Returns an iterator over this system's component signature.
	 */
	@Override
	public final Iterator<Class<? extends Component>> iterator() {
		return signature.iterator();
	}

	@Override
	public String toString() {
		return "GameSystem{" +
				"signature=" + signature +
				", entities=" + entities +
				", sampler=" + sampler +
				'}';
	}

	private static class DummyComponent implements Component {}
}
