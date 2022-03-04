package dev.kkorolyov.pancake.platform;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.EntityPool;

import java.util.Arrays;
import java.util.Collection;

/**
 * Performs work on entities matching a certain component signature.
 */
public abstract class GameSystem {
	private final Collection<Class<? extends Component>> signature;
	private EntityPool entities;

	/**
	 * Constructs a new system with {@code signature} defining the minimum component set of entities processed by this system.
	 */
	@SafeVarargs
	protected GameSystem(Class<? extends Component>... signature) {
		this.signature = Arrays.stream(signature).toList();
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
	protected Entity create() {
		return entities.create();
	}
	/**
	 * Destroys entity by {@code id}.
	 */
	protected void destroy(int id) {
		entities.destroy(id);
	}

	/**
	 * Attaches resources to this system.
	 */
	public void attach(EntityPool entities) {
		this.entities = entities;
	}

	/**
	 * Executes a single update on this system with {@code dt} {@code (ns)} timestep.
	 */
	public void update(long dt) {
		before();
		for (Entity entity : entities.get(getSignature())) {
			update(entity, dt);
		}
		after();
	}

	/** @return system required component signature */
	public final Iterable<Class<? extends Component>> getSignature() {
		return signature;
	}
}
