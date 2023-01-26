package dev.kkorolyov.pancake.platform;

import dev.kkorolyov.pancake.platform.entity.EntityPool;
import dev.kkorolyov.pancake.platform.utility.ArgVerify;
import dev.kkorolyov.pancake.platform.utility.Sampler;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Updates a group of {@link GameSystem}s as a unit.
 * NOTE: sharing the same system between pipelines can lead to undefined behavior.
 */
public sealed class Pipeline implements Iterable<GameSystem> {
	private final GameSystem[] systems;

	// shared resources
	Suspend suspend;

	private final Sampler sampler = new Sampler();

	/**
	 * Constructs a new pipeline running {@code systems}.
	 */
	public Pipeline(GameSystem... systems) {
		this.systems = systems.clone();
	}

	/**
	 * Returns a variant of this pipeline that ensures its systems update at a constant {@code frequency} times per second.
	 */
	public Pipeline fixed(int frequency) {
		return new Fixed(frequency, systems);
	}
	/**
	 * Returns a variant of this pipeline that does not update when its attached {@link Suspend#isActive()}.
	 */
	public Pipeline suspendable() {
		return new Suspendable(systems);
	}

	/**
	 * Updates this pipeline by {@code dt} elapsed ns.
	 * A {@code dt < 0} is supported (can imply e.g. update in reverse).
	 */
	void update(long dt) {
		sampler.reset();
		for (GameSystem system : systems) system.update(dt);
		sampler.sample();
	}

	/**
	 * Attaches resources to this pipeline.
	 */
	void attach(EntityPool entities, Suspend suspend) {
		this.suspend = suspend;

		for (GameSystem system : systems) system.attach(entities);
	}
	/**
	 * Detaches all resources from this pipeline.
	 */
	void detach() {
		suspend = null;

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
				", suspend=" + suspend +
				", sampler=" + sampler +
				'}';
	}

	private static final class Fixed extends Pipeline {
		private final long delay;

		private long lag;

		private Fixed(int frequency, GameSystem[] systems) {
			super(systems);

			delay = (long) 1e9 / ArgVerify.greaterThan("frequency", 0, frequency);
		}

		@Override
		void update(long dt) {
			lag += Math.abs(dt);
			if (lag >= delay) {
				long signedTimestep = dt < 0 ? -delay : delay;

				do {
					super.update(signedTimestep);
					lag -= delay;
				} while (lag > delay);
			}
		}

		@Override
		public String toString() {
			return super.toString() + ".Fixed{delay=" + delay + "}";
		}
	}

	private static final class Suspendable extends Pipeline {
		private Suspendable(GameSystem[] systems) {
			super(systems);
		}

		@Override
		void update(long dt) {
			if (!suspend.isActive()) {
				super.update(dt);
			}
		}

		@Override
		public String toString() {
			return super.toString() + ".Suspendable";
		}
	}
}
