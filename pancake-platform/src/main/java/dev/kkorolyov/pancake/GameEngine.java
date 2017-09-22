package dev.kkorolyov.pancake;

import dev.kkorolyov.pancake.entity.EntityPool;
import dev.kkorolyov.pancake.event.EventBroadcaster;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceLoader;

/**
 * Central game management module.
 * Serves as the link between entities with components containing data and systems specifying business logic.
 */
public class GameEngine {
	private final EventBroadcaster events = new EventBroadcaster();
	private final EntityPool entities = new EntityPool(events);
	private final Map<GameSystem, Limiter> systems = new LinkedHashMap<>();

	/**
	 * Constructs a new engine pre-populated with systems provided by the service loader.
	 */
	public GameEngine() {
		this(ServiceLoader.load(GameSystem.class));
	}
	/**
	 * Constructs a new engine pre-populated with systems.
	 * @param systems attached systems
	 */
	public GameEngine(GameSystem... systems) {
		this(Arrays.asList(systems));
	}
	/**
	 * Constructs a new engine pre-populated with systems.
	 * @param systems attached systems
	 */
	public GameEngine(Iterable<GameSystem> systems) {
		systems.forEach(this::add);
	}
	
	/**
	 * Applies a single timestep of system updates on all applicable entities.
	 * @param dt seconds elapsed since last update
	 */
	public void update(float dt) {
		events.broadcast();

		for (Entry<GameSystem, Limiter> entry : systems.entrySet()) {
			if (!entry.getValue().isReady(dt)) continue;

			GameSystem system = entry.getKey();

			PerformanceCounter.start();

			system.before(dt);

			entities.get(system.getSignature(), system.getComparator())
							.sequential()	// Avoid asynchronous update issues
							.forEach(entity -> system.update(entity, dt));

			system.after(dt);

			PerformanceCounter.end(system);
		}
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
		system.setEvents(events);

		systems.put(system, new Limiter(Math.max(0, updateInterval)));

		system.attach();
	}

	/** @param system removed system */
	public void remove(GameSystem system) {
		system.setEvents(null);

		systems.remove(system);

		system.detach();
	}

	/** @return entities handled by this engine */
	public EntityPool getEntities() {
		return entities;
	}
	/** @return event queue and broadcaster used by this engine */
	public EventBroadcaster getEvents() {
		return events;
	}
}
