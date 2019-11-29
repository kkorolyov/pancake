package dev.kkorolyov.pancake.platform;

import dev.kkorolyov.pancake.platform.entity.EntityPool;
import dev.kkorolyov.pancake.platform.event.internal.ManagedEventBroadcaster;
import dev.kkorolyov.pancake.platform.utility.PerformanceCounter;
import dev.kkorolyov.simplefiles.Providers;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Central game management module.
 * Serves as the link between entities with components containing data and systems specifying business logic.
 */
public class GameEngine {
	private final ManagedEventBroadcaster events;
	private final EntityPool entities;
	private final PerformanceCounter performanceCounter = new PerformanceCounter();
	private final Collection<GameSystem> systems = new LinkedHashSet<>();
	private final SharedResources resources;

	/**
	 * Constructs a new game engine populated with all {@link GameSystem} providers on the classpath.
	 */
	public GameEngine(ManagedEventBroadcaster events, EntityPool entities) {
		this(events, entities, Providers.fromDescriptor(GameSystem.class).stream()::iterator);
	}
	/**
	 * @see #GameEngine(ManagedEventBroadcaster, EntityPool, Iterable)
	 */
	public GameEngine(ManagedEventBroadcaster events, EntityPool entities, GameSystem... systems) {
		this(events, entities, Arrays.asList(systems));
	}
	/**
	 * Constructs a new game engine.
	 * @param events attached event broadcaster
	 * @param entities attached entity pool
	 * @param systems attached systems
	 */
	public GameEngine(ManagedEventBroadcaster events, EntityPool entities, Iterable<GameSystem> systems) {
		this.events = events;
		this.entities = entities;
		resources = new SharedResources(events, performanceCounter);

		systems.forEach(this::add);
	}

	/**
	 * Proceeds the simulation by 1 tick.
	 * <pre>
	 * All queued events are broadcast
	 * All entities apply each of their attached actions
	 * Each system updates all entities it is applicable to
	 * </pre>
	 * @param dt {@code ns} elapsed since last update
	 */
	public void update(long dt) {
		events.broadcast();

		for (GameSystem system : systems) {
			if (system.getLimiter().isReady(dt)) {
				long systemDt = system.getLimiter().consumeElapsed();

				performanceCounter.start();

				system.before(systemDt);

				entities.stream(system.getSignature()).forEach(entity -> system.update(entity, systemDt));

				system.after(systemDt);

				performanceCounter.end(system);
			}
		}
		performanceCounter.tick();
	}

	/** @param system system to add */
	public void add(GameSystem system) {
		systems.add(system.setResources(resources));
		system.attach();
	}
	/** @param system removed system */
	public void remove(GameSystem system) {
		if (systems.remove(system)) {
			system.detach();
			system.setResources(null);
		}
	}
}
