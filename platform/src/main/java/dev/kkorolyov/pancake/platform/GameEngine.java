package dev.kkorolyov.pancake.platform;

import dev.kkorolyov.pancake.platform.entity.EntityPool;
import dev.kkorolyov.pancake.platform.utility.Sampler;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Central game management module.
 * Serves as the link between entities with components containing data and systems specifying business logic.
 * Updates a group of {@link Pipeline}s.
 * NOTE: sharing the same pipeline between engines can lead to undefined behavior.
 */
public final class GameEngine implements Iterable<Pipeline> {
	private final EntityPool entities = new EntityPool();
	private Pipeline[] pipelines;

	private volatile boolean active;
	private volatile double speed = 1;

	private final Sampler sampler = new Sampler();

	/**
	 * Constructs a new game engine updating {@code pipelines}.
	 */
	public GameEngine(Pipeline... pipelines) {
		setPipelines(pipelines);
	}

	/**
	 * Starts this engine on the current thread if it is not currently active.
	 * An active game loop continuously updates its pipelines by {@code speed}-modified elapsed time per update cycle.
	 */
	public void start() {
		if (!active) {
			active = true;
			long last = System.nanoTime();

			while (active) {
				long now = System.nanoTime();
				long elapsed = now - last;
				long dt = (long) (elapsed * speed);
				last = now;

				sampler.reset();
				for (Pipeline pipeline : pipelines) pipeline.update(dt);
				sampler.sample();
			}
		}
	}
	/**
	 * Halts execution of this engine if it is currently active.
	 */
	public void stop() {
		active = false;
	}

	/**
	 * Returns the active state of this engine.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Returns the modifier applied to all pipelines' update timestep.
	 */
	public double getSpeed() {
		return speed;
	}
	/**
	 * Sets {@code speed} as the modifier applied to all pipelines' update timestep.
	 * A negative value effectively reverses time-aware pipelines.
	 * A {@code 0} value effectively stops time-aware pipelines.
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	/**
	 * Returns this engine's entity pool.
	 */
	public EntityPool getEntities() {
		return entities;
	}

	/**
	 * Returns this engine's sampler.
	 */
	public Sampler getSampler() {
		return sampler;
	}

	/**
	 * Detaches all current pipelines and attaches {@code pipelines}.
	 */
	public void setPipelines(Pipeline... pipelines) {
		for (Pipeline pipeline : pipelines) pipeline.detach();

		this.pipelines = pipelines.clone();
		for (Pipeline pipeline : this.pipelines) pipeline.attach(entities);
	}

	/**
	 * Returns the number of pipelines in engine.
	 */
	public int size() {
		return pipelines.length;
	}

	/**
	 * Returns an iterator over this engine's pipelines.
	 */
	@Override
	public Iterator<Pipeline> iterator() {
		return Arrays.stream(pipelines).iterator();
	}

	@Override
	public String toString() {
		return "GameEngine{" +
				", entities=" + entities +
				", pipelines=" + Arrays.toString(pipelines) +
				", sampler=" + sampler +
				'}';
	}
}
