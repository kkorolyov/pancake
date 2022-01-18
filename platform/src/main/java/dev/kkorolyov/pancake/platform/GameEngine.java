package dev.kkorolyov.pancake.platform;

import dev.kkorolyov.pancake.platform.entity.EntityPool;
import dev.kkorolyov.pancake.platform.event.EventLoop;
import dev.kkorolyov.pancake.platform.utility.PerfMonitor;
import dev.kkorolyov.pancake.platform.utility.Sampler;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Central game management module.
 * Serves as the link between entities with components containing data and systems specifying business logic.
 */
public final class GameEngine {
	private final EventLoop.Broadcasting events;
	private final EntityPool entities;
	private final Collection<GameSystem> systems = new LinkedHashSet<>();

	private final PerfMonitor perfMonitor = new PerfMonitor();

	/**
	 * Constructs a new game engine.
	 * @param events attached event broadcaster
	 * @param entities attached entity pool
	 * @param systems attached systems
	 */
	public GameEngine(EventLoop.Broadcasting events, EntityPool entities, Iterable<GameSystem> systems) {
		this.events = events;
		this.entities = entities;

		systems.forEach(this::add);
	}

	/**
	 * Proceeds the simulation by 1 tick.
	 * <pre>
	 * All queued events are broadcast
	 * Each system updates all entities it is applicable to
	 * </pre>
	 * @param dt {@code ns} elapsed since last update
	 */
	public void update(long dt) {
		events.broadcast();

		for (GameSystem system : systems) {
			if (system.getLimiter().isReady(dt)) {
				long systemDt = system.getLimiter().consumeElapsed();

				Sampler sampler = perfMonitor.getSystem(system);

				system.before(systemDt);

				for (EntityPool.ManagedEntity entity : entities.get(system.getSignature())) {
					system.update(entity, systemDt);
				}

				system.after(systemDt);

				sampler.sample();
			}
		}
		perfMonitor.getEngine().sample();
	}

	/**
	 * Attaches a system to this engine.
	 * @param system system to add
	 * @see GameSystem#attach()
	 */
	public void add(GameSystem system) {
		systems.add(system);
		system.setEvents(events);
		system.attach();
	}
	/**
	 * Detaches a system from this engine.
	 * @param system system to remove
	 * @see GameSystem#detach()
	 */
	public void remove(GameSystem system) {
		if (systems.remove(system)) {
			system.detach();
			system.setEvents(null);
		}
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
				"events=" + events +
				", entities=" + entities +
				", systems=" + systems +
				", perfMonitor=" + perfMonitor +
				'}';
	}
}
