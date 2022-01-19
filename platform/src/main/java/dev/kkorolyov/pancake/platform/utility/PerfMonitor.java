package dev.kkorolyov.pancake.platform.utility;

import dev.kkorolyov.pancake.platform.GameSystem;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Provides time sampling for a {@link dev.kkorolyov.pancake.platform.GameEngine} and its {@link GameSystem}s.
 */
public final class PerfMonitor {
	private final Sampler engine = new Sampler();
	private final Map<GameSystem, Sampler> systems = new LinkedHashMap<>();

	/**
	 * Returns the sampler for the main engine.
	 */
	public Sampler getEngine() {
		return engine;
	}

	/**
	 * Returns the sampler for {@code system}.
	 */
	public Sampler getSystem(GameSystem system) {
		return systems.computeIfAbsent(system, k -> new Sampler());
	}

	/**
	 * Returns all current {@code (system, sampler)} pairs.
	 */
	public Map<GameSystem, Sampler> getSystems() {
		return systems;
	}
}
