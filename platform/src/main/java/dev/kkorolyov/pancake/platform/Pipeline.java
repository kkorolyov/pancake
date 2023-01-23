package dev.kkorolyov.pancake.platform;

import dev.kkorolyov.pancake.platform.entity.EntityPool;
import dev.kkorolyov.pancake.platform.utility.Sampler;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Updates a group of {@link GameSystem}s as a unit.
 * NOTE: sharing the same system between pipelines can lead to undefined behavior.
 */
public final class Pipeline implements Iterable<GameSystem> {
	private final GameSystem[] systems;

	private final long delay;
	private long lag;

	private final Sampler sampler = new Sampler();

	/**
	 * Constructs a new pipeline running {@code systems}.
	 */
	public Pipeline(GameSystem... systems) {
		this(0, systems);
	}
	private Pipeline(long delay, GameSystem... systems) {
		this.delay = delay;
		this.systems = systems.clone();
	}

	/**
	 * Returns a pipeline that runs this pipeline's systems with target {@code frequency}.
	 * The returned pipeline ensures that its systems update at a constant {@code frequency} times per second.
	 */
	public Pipeline withFrequency(int frequency) {
		return new Pipeline((long) 1e9 / frequency, systems);
	}

	/**
	 * Updates this pipeline by {@code dt} elapsed ns.
	 * A {@code dt < 0} is supported (can imply e.g. update in reverse).
	 */
	void update(long dt) {
		lag += Math.abs(dt);
		if (lag >= delay) {
			long timestep = delay > 0 ? delay : lag;
			long signedTimestep = dt < 0 ? -timestep : timestep;

			do {
				sampler.reset();
				for (GameSystem system : systems) system.update(signedTimestep);
				sampler.sample();

				lag -= timestep;
			} while (lag > delay);
		}
	}

	/**
	 * Attaches resources to this pipeline.
	 */
	void attach(EntityPool entities) {
		for (GameSystem system : systems) system.attach(entities);
	}
	/**
	 * Detaches all resources from this pipeline.
	 */
	void detach() {
		for (GameSystem system : systems) system.detach();
	}

	/**
	 * Returns this pipeline's sampler.
	 */
	public Sampler getSampler() {
		return sampler;
	}

	/**
	 * Returns the number of systems in this pipeline.
	 */
	public int size() {
		return systems.length;
	}

	/**
	 * Returns an iterator over this pipeline's systems.
	 */
	@Override
	public Iterator<GameSystem> iterator() {
		return Arrays.stream(systems).iterator();
	}

	@Override
	public String toString() {
		return "Pipeline{" +
				"systems=" + Arrays.toString(systems) +
				", delay=" + delay +
				", lag=" + lag +
				", sampler=" + sampler +
				'}';
	}
}
