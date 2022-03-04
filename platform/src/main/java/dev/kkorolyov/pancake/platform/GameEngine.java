package dev.kkorolyov.pancake.platform;

import dev.kkorolyov.pancake.platform.entity.EntityPool;
import dev.kkorolyov.pancake.platform.utility.PerfMonitor;

import java.util.Collection;
import java.util.List;

/**
 * Central game management module.
 * Serves as the link between entities with components containing data and systems specifying business logic.
 */
public final class GameEngine {
	private final EntityPool entities = new EntityPool();
	private final Pipeline[] pipelines;

	private final PerfMonitor perfMonitor = new PerfMonitor();

	private volatile boolean active;
	private volatile double speed = 1;

	/**
	 * Constructs a new game engine updating {@code pipelines}.
	 */
	public GameEngine(Pipeline... pipelines) {
		this.pipelines = pipelines.clone();
		for (Pipeline pipeline : this.pipelines) pipeline.attach(entities, perfMonitor);
	}

	/**
	 * Starts this engine on the current thread if it is not currently active.
	 * An active game loop continuously updates its pipelines by {@code speed}-modified elapsed time per update cycle.
	 */
	public void start() {
		if (!active) {
			active = true;
			perfMonitor.getEngine().reset();
			long last = System.nanoTime();

			while (active) {
				long now = System.nanoTime();
				long elapsed = now - last;
				long dt = (long) (elapsed * speed);
				last = now;

				for (Pipeline pipeline : pipelines) pipeline.update(dt);
				perfMonitor.getEngine().sample();
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
	 * Returns this engine's update pipelines.
	 */
	public Collection<Pipeline> getPipelines() {
		return List.of(pipelines);
	}

	/**
	 * Returns this engine's performance monitor.
	 */
	public PerfMonitor getPerfMonitor() {
		return perfMonitor;
	}

	@Override
	public String toString() {
		return "GameEngine{" +
				", entities=" + entities +
				", pipelines=" + pipelines +
				", perfMonitor=" + perfMonitor +
				'}';
	}
}
