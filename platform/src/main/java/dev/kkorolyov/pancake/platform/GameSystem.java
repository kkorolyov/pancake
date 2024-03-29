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
public abstract class GameSystem implements Iterable<Class<? extends Component>>, Debuggable {
	private final Collection<Class<? extends Component>> signature;

	// shared resources
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
	 * Returns a system of {@code name} that runs {@code op} once per update.
	 * Useful for simple, pipeline-spanning hooks like setting up rendering, swapping buffers, or polling events.
	 */
	public static GameSystem hook(String name, Runnable op) {
		// dummy component to avoid iterating over all entities
		return new GameSystem(DummyComponent.class) {
			@Override
			protected void update(Entity entity, long dt) {}

			@Override
			protected void after(long dt) {
				op.run();
			}

			@Override
			public String getDebugName() {
				return name;
			}
		};
	}

	/**
	 * Invoked on each entity affected by this system in the current update cycle.
	 * @param entity entity to update
	 * @param dt {@code ns} timestep for the current update cycle - approximately equivalent to the time elapsed since the last cycle
	 */
	protected abstract void update(Entity entity, long dt);

	/**
	 * Invoked at the beginning of an update cycle with the current {@code dt} {@code ns} timestep.
	 * Intended for any static pre-update logic.
	 */
	protected void before(long dt) {}
	/**
	 * Invoked at the end of an update cycle with the current {@code dt} {@code ns} timestep.
	 * Intended for any static post-update logic.
	 */
	protected void after(long dt) {}

	/**
	 * Returns a new entity created from the attached {@link EntityPool}.
	 */
	protected final Entity create() {
		return entities.create();
	}
	/**
	 * Destroys entity by {@code id} from the attached {@link EntityPool}.
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
	void update(long dt) {
		sampler.start();

		before(dt);
		for (Entity entity : entities.get(signature)) update(entity, dt);
		after(dt);

		sampler.end();
	}

	/**
	 * Returns this system's sampler.
	 */
	public final Sampler getSampler() {
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

	/**
	 * Returns the simple name of this object's class.
	 * If it has no simple name - e.g. anonymous class - returns the class full name.
	 */
	@Override
	public String getDebugName() {
		String simpleName = getClass().getSimpleName();
		return simpleName.isEmpty() ? getClass().getName() : simpleName;
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
