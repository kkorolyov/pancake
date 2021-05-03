package dev.kkorolyov.pancake.platform;

import dev.kkorolyov.pancake.platform.entity.EntityPool;
import dev.kkorolyov.pancake.platform.event.EventLoop;
import dev.kkorolyov.pancake.platform.service.Services;
import dev.kkorolyov.pancake.platform.utility.DebugRenderer;
import dev.kkorolyov.pancake.platform.utility.PerformanceCounter;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.ServiceLoader;

/**
 * Central game management module.
 * Serves as the link between entities with components containing data and systems specifying business logic.
 */
public final class GameEngine {
	private final EventLoop.Broadcasting events;
	private final EntityPool entities;
	private final Collection<GameSystem> systems = new LinkedHashSet<>();

	private final PerformanceCounter performanceCounter = new PerformanceCounter();
	private final DebugRenderer debugRenderer;

	/**
	 * Constructs a new game engine populated with all {@link GameSystem} providers on the classpath.
	 */
	public GameEngine(EventLoop.Broadcasting events, EntityPool entities) {
		this(events, entities, ServiceLoader.load(GameSystem.class).stream().map(ServiceLoader.Provider::get)::iterator);
	}
	/**
	 * @see #GameEngine(EventLoop.Broadcasting, EntityPool, Iterable)
	 */
	public GameEngine(EventLoop.Broadcasting events, EntityPool entities, GameSystem... systems) {
		this(events, entities, Arrays.asList(systems));
	}
	/**
	 * Constructs a new game engine.
	 * @param events attached event broadcaster
	 * @param entities attached entity pool
	 * @param systems attached systems
	 */
	public GameEngine(EventLoop.Broadcasting events, EntityPool entities, Iterable<GameSystem> systems) {
		this(
				events,
				entities,
				systems,
				new DebugRenderer(Services.renderMedium())
		);
	}
	GameEngine(EventLoop.Broadcasting events, EntityPool entities, Iterable<GameSystem> systems, DebugRenderer debugRenderer) {
		this.events = events;
		this.entities = entities;

		systems.forEach(this::add);

		this.debugRenderer = debugRenderer;
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

				performanceCounter.start();

				system.before(systemDt);

				entities.stream(system.getSignature()).forEach(entity -> system.update(entity, systemDt));

				system.after(systemDt);

				performanceCounter.end(system);
			}
		}
		performanceCounter.tick();

		debugRenderer.render(performanceCounter);
	}

	/**
	 * Attaches a system to this engine.
	 * @param system system to add
	 * @see GameSystem#attach()
	 */
	public void add(GameSystem system) {
		systems.add(system);
		system.setResources(events);
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
			system.setResources(null);
		}
	}
	@Override
	public String toString() {
		return "GameEngine{" +
				"events=" + events +
				", entities=" + entities +
				", performanceCounter=" + performanceCounter +
				", systems=" + systems +
				", debugRenderer=" + debugRenderer +
				'}';
	}
}
