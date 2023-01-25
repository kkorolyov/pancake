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

	// options
	private long delay;
	private boolean suspendable;

	// shared resources
	private Suspend suspend;

	private long lag;

	private final Sampler sampler = new Sampler();

	/**
	 * Constructs a new pipeline running {@code systems}.
	 */
	public Pipeline(GameSystem... systems) {
		this.systems = systems.clone();
	}

	/**
	 * Configures this pipeline to ensure that its systems update at a constant {@code frequency} times per second.
	 * Returns this pipeline as a convenience for chaining operations.
	 */
	public Pipeline withFrequency(int frequency) {
		delay = (long) 1e9 / frequency;
		return this;
	}
	/**
	 * Configures this pipeline to ignore calls to {@link #update(long)} when its attached {@link Suspend#isActive()}.
	 * Returns this pipeline as a convenience for chaining operations.
	 */
	public Pipeline withSuspendable() {
		suspendable = true;
		return this;
	}

	/**
	 * Updates this pipeline by {@code dt} elapsed ns.
	 * A {@code dt < 0} is supported (can imply e.g. update in reverse).
	 * Does nothing if {@link #isSuspended()}.
	 */
	void update(long dt) {
		if (!(suspendable && suspend.isActive())) {
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
	}

	/**
	 * Attaches resources to this pipeline.
	 */
	void attach(EntityPool entities, Suspend suspend) {
		this.suspend = suspend;

		for (GameSystem system : systems) system.attach(entities, suspend);
	}
	/**
	 * Detaches all resources from this pipeline.
	 */
	void detach() {
		suspend = null;

		for (GameSystem system : systems) system.detach();
	}

	/**
	 * Returns {@code true} if this pipeline accepts suspend requests and its attached {@link Suspend#isActive()}.
	 */
	public boolean isSuspended() {
		return suspendable && suspend.isActive();
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
				", suspendable=" + suspendable +
				", suspend=" + suspend +
				", lag=" + lag +
				", sampler=" + sampler +
				'}';
	}
}
