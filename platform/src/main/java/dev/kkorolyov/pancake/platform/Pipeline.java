package dev.kkorolyov.pancake.platform;

import dev.kkorolyov.pancake.platform.entity.EntityPool;
import dev.kkorolyov.pancake.platform.utility.PerfMonitor;
import dev.kkorolyov.pancake.platform.utility.Sampler;

import java.util.Collection;
import java.util.List;

/**
 * Updates a group of {@link GameSystem}s as a unit.
 */
public final class Pipeline {
	private final GameSystem[] systems;
	private PerfMonitor perfMonitor;

	private final long delay;
	private long lag;

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
	 */
	public void update(long dt) {
		lag += Math.abs(dt);
		if (lag > 0 && lag >= delay) {
			long timestep = delay > 0 ? delay : lag;
			long signedTimestep = dt < 0 ? -timestep : timestep;

			while (lag >= delay) {
				for (GameSystem system : systems) {
					Sampler sampler = perfMonitor.getSystem(system);
					sampler.reset();

					system.update(signedTimestep);

					sampler.sample();
				}

				lag -= timestep;
			}
		}
	}

	/**
	 * Attaches resources to this group.
	 */
	public void attach(EntityPool entities, PerfMonitor perfMonitor) {
		this.perfMonitor = perfMonitor;

		for (GameSystem system : systems) {
			system.attach(entities);
		}
	}

	/**
	 * Returns this group's systems.
	 */
	public Collection<GameSystem> getSystems() {
		return List.of(systems);
	}
}