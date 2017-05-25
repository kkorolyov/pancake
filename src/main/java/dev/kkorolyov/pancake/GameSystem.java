package dev.kkorolyov.pancake;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Performs work on some set of entities.
 */
public abstract class GameSystem {
	private final Set<Class<? extends Component>> requiredComponents = new HashSet<>();
	private Engine engine;
	
	/**
	 * Constructs a new system.
	 * @param requiredComponents minimum set of component types employed by entities on which this system works
	 */
	@SafeVarargs
	public GameSystem(Class<? extends Component>... requiredComponents) {
		this.requiredComponents.addAll(Arrays.asList(requiredComponents));
	}
	
	void register(Engine engine) {
		this.engine = engine;
	}
	
	/** @return	minimum set of component types employed by entities on which this system works */
	public Set<Class<? extends Component>> requires() {
		return requiredComponents;
	}
	
	/** @return entities on which this system performs work */
	protected EntityManager getEntities() {
		return engine.getEntities();
	}
	
	/**
	 * Updates applicable entities.
	 * @param dt seconds elapsed since last update
	 * @return number of updated entities
	 */
	public abstract int update(float dt);
}
