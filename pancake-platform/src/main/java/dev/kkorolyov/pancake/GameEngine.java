package dev.kkorolyov.pancake;

import dev.kkorolyov.pancake.entity.EntityPool;
import dev.kkorolyov.pancake.event.EventBroadcaster;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Central game management module.
 * Serves as the link between entities with components containing data and systems specifying business logic.
 */
public class GameEngine {
	private final EventBroadcaster events = new EventBroadcaster();
	private final EntityPool entities = new EntityPool(events);
	private final Set<GameSystem> systems = new LinkedHashSet<>();
	
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
		for (GameSystem system : systems) add(system);
	}
	
	/**
	 * Applies a single timestep of system updates on all applicable entities.
	 * @param dt seconds elapsed since last update
	 */
	public void update(float dt) {
		events.broadcast();

		for (GameSystem system : systems) {
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
		system.setEvents(events);

		systems.add(system);

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
