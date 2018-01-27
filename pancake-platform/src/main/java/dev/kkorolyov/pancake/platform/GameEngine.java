package dev.kkorolyov.pancake.platform;

import dev.kkorolyov.pancake.platform.entity.ManagedEntityPool;
import dev.kkorolyov.pancake.platform.event.management.ManagedEventBroadcaster;
import dev.kkorolyov.pancake.platform.utility.Limiter;
import dev.kkorolyov.pancake.platform.utility.PerformanceCounter;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Central game management module.
 * Serves as the link between entities with components containing data and systems specifying business logic.
 */
public class GameEngine {
	private final ManagedEventBroadcaster events;
	private final ManagedEntityPool entities;
	private final PerformanceCounter performanceCounter = new PerformanceCounter();
	private final Map<GameSystem, Limiter> systems = new LinkedHashMap<>();

	/**
	 * Constructs a new game engine populated with all {@link GameSystem} providers on the classpath.
	 */
	public GameEngine(ManagedEventBroadcaster events, ManagedEntityPool entities) {
		this(events, entities, Resources.providers(GameSystem.class));
	}
	/**
	 * @see #GameEngine(ManagedEventBroadcaster, ManagedEntityPool, Iterable)
	 */
	public GameEngine(ManagedEventBroadcaster events, ManagedEntityPool entities, GameSystem... systems) {
		this(events, entities, Arrays.asList(systems));
	}
	/**
	 * Constructs a new game engine.
	 * @param events attached event broadcaster
	 * @param entities attached entity pool
	 * @param systems attached systems
	 */
	public GameEngine(ManagedEventBroadcaster events, ManagedEntityPool entities, Iterable<GameSystem> systems) {
		this.events = events;
		this.entities = entities;

		systems.forEach(this::add);
	}
	
	/**
	 * Proceeds the simulation by 1 tick.
	 * <pre>
	 * All queued events are broadcast
	 * All entities apply each of their attached actions
	 * Each system updates all entities it is applicable to
	 * </pre>
	 * @param dt seconds elapsed since last update
	 */
	public void update(float dt) {
		events.broadcast();
		entities.applyActions();

		for (Entry<GameSystem, Limiter> entry : systems.entrySet()) {
			if (!entry.getValue().isReady(dt)) continue;

			GameSystem system = entry.getKey();

			performanceCounter.start();

			system.before(dt);

			entities.forEachMatching(system.getSignature(), id -> system.update(id, dt));

			system.after(dt);

			performanceCounter.end(system);
		}
		performanceCounter.tick();
	}

	/** @param system added system */
	public void add(GameSystem system) {
		add(system, 0);
	}
	/**
	 * @param system added system
	 * @param updateInterval minimum elapsed seconds between updates of {@code system}, constrained {@code >= 0}
	 */
	public void add(GameSystem system, float updateInterval) {
		system.share(entities, events, performanceCounter);

		systems.put(system, new Limiter(Math.max(0, updateInterval)));

		system.attach();
	}

	/** @param system removed system */
	public void remove(GameSystem system) {
		system.share(null, null, null);

		systems.remove(system);

		system.detach();
	}
}
