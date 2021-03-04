package dev.kkorolyov.pancake.platform.media.graphic;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;

import static dev.kkorolyov.flopple.collections.Iterables.append;

/**
 * A collection of renderable artifacts rendered as a unit.
 * @param <T> aggregated renderable type
 */
public final class CompositeRenderable<T extends Renderable> implements Renderable, Iterable<T> {
	private final Collection<T> delegates = new LinkedHashSet<>();

	/** @see #CompositeRenderable(Iterable) */
	@SafeVarargs
	public CompositeRenderable(T delegate, T... delegates) {
		this(append(delegate, delegates));
	}
	/**
	 * Constructs a new composite renderable.
	 * @param delegates delegates to render in order
	 */
	public CompositeRenderable(Iterable<T> delegates) {
		delegates.forEach(this.delegates::add);
	}

	@Override
	public void render(RenderTransform transform) {
		for (T delegate : this) {
			delegate.render(transform);
		}
	}

	@Override
	public Iterator<T> iterator() {
		return delegates.iterator();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;

		CompositeRenderable<?> o = (CompositeRenderable<?>) obj;
		return Objects.equals(delegates, o.delegates);
	}
	@Override
	public int hashCode() {
		return Objects.hash(delegates);
	}
}
