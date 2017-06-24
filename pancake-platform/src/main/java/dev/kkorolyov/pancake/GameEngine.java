package dev.kkorolyov.pancake;

import java.util.*;
import java.util.Map.Entry;

import dev.kkorolyov.pancake.entity.EntityPool;
import dev.kkorolyov.pancake.event.EventBroadcaster;
import dev.kkorolyov.simplelogs.Logger;

/**
 * Central game management module.
 * Serves as the link between entities with components containing data and systems specifying business logic.
 */
public class GameEngine {
	private static final Logger log = Config.getLogger(GameEngine.class);
	private static final Map<GameSystem, Long> systemUsage = new HashMap<>();
	private static int usageSamples;

	private final EventBroadcaster events = new EventBroadcaster();
	private final EntityPool entities = new EntityPool(events);
	private final Set<GameSystem> systems = new LinkedHashSet<>();
	
	/**
	 * Constructs a new engine pre-populated with systems.
	 */
	public GameEngine(GameSystem... systems) {
		this(Arrays.asList(systems));
	}
	/**
	 * Constructs a new engine pre-populated with systems.
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
			long start = System.nanoTime();

			system.before(dt);

			entities.get(system.getSignature(), system.getComparator())
							.sequential()	// Avoid asynchronous update issues
							.forEach(entity -> system.update(entity, dt));

			system.after(dt);

			Long lastUsage = systemUsage.get(system);
			if (lastUsage == null) lastUsage = 0L;
			systemUsage.put(system, lastUsage + (System.nanoTime() - start));
		}
		usageSamples++;
		logUsage();
	}
	private static void logUsage() {
		String sampleSize = Config.config.get("systemUsageSampleSize");
		if (sampleSize != null) {
			if (usageSamples >= Integer.parseInt(sampleSize)) {
				StringJoiner usageResult = new StringJoiner(System.lineSeparator());
				usageResult.add("System time usage over " + usageSamples + " ticks");

				for (Entry<GameSystem, Long> entry : systemUsage.entrySet()) {
					String systemName = entry.getKey().getClass().getName();
					long totalMS = entry.getValue() / 1000000;

					entry.setValue(0L);
					usageResult.add(systemName + ": " + totalMS + "ms");
				}
				usageSamples = 0;
				log.debug("{}", usageResult);
			}
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
