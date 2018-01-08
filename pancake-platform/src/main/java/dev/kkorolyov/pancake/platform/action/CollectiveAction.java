package dev.kkorolyov.pancake.platform.action;

import dev.kkorolyov.pancake.platform.entity.EntityPool;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * An {@link Action} which applies a collection of delegate actions.
 */
public class CollectiveAction extends Action {
	private final Set<Action> delegates = new HashSet<>();

	/** @see #CollectiveAction(Iterable)  */
	public CollectiveAction(Action... delegates) {
		this(Arrays.asList(delegates));
	}
	/**
	 * Constructs a new collective action.
	 * @param delegates contained actions, duplicates are ignored
	 */
	public CollectiveAction(Iterable<Action> delegates) {
		delegates.forEach(this.delegates::add);
	}

	/**
	 * Applies all contained, accepting actions to an entity.
	 */
	@Override
	protected void apply(UUID id, EntityPool entities) {
		for (Action delegate : delegates) delegate.accept(id, entities);
	}

	/** @return all delegate actions */
	public Set<Action> getDelegates() {
		return delegates;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || !getClass().isAssignableFrom(obj.getClass())) return false;

		CollectiveAction o = (CollectiveAction) obj;
		return Objects.equals(delegates, o.delegates);
	}
	@Override
	public int hashCode() {
		return Objects.hash(delegates);
	}
}
