package dev.kkorolyov.pancake.platform.entity;

import dev.kkorolyov.flub.data.SparseMultiset;

import java.util.Iterator;

/**
 * A set of uniquely-identified "component-bag" entities.
 */
public final class EntityPool implements Iterable<Entity> {
	private final SparseMultiset<Entity, Class<? extends Component>> pool = new SparseMultiset<>();

	/**
	 * @param id ID of entity to get
	 * @return entity with ID {@code id}, or {@code null} if no such entity
	 */
	public Entity get(int id) {
		return pool.get(id);
	}
	/**
	 * @param signature signature to match
	 * @return all entities in this pool masking {@code signature}
	 */
	public Iterable<Entity> get(Iterable<Class<? extends Component>> signature) {
		return pool.get(signature);
	}

	/**
	 * Creates a new, empty entity attached to this pool.
	 * @return created entity
	 */
	public Entity create() {
		return new Entity(pool);
	}
	/**
	 * Removes an entity from this pool.
	 * @param id ID of entity to remove
	 */
	public void destroy(int id) {
		pool.remove(id);
	}

	@Override
	public Iterator<Entity> iterator() {
		return pool.iterator();
	}

	@Override
	public String toString() {
		return pool.toString();
	}
}
