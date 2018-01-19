package dev.kkorolyov.pancake.platform.entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

/**
 * A distinct combination of registered component types.
 */
public class Signature implements Comparable<Signature> {
	private static final HashMap<Class<? extends Component>, Long> indexMap = new HashMap<>();
	private static long indexCounter;

	private final Collection<Class<? extends Component>> types = new HashSet<>();
	private long signature;

	/**
	 * Constructs a new signature from a set of component types.
	 * @param types types defining signature
	 */
	@SafeVarargs
	public Signature(Class<? extends Component>... types) {
		this(Arrays.asList(types));
	}
	/**
	 * Constructs a new signature from a set of component types.
	 * @param types types defining signature
	 */
	public Signature(Iterable<Class<? extends Component>> types) {
		if (types != null) {
			for (Class<? extends Component> type : types) {
				add(type);
			}
		}
	}

	private static long maskOf(Class<? extends Component> type) {
		return indexMap.computeIfAbsent(type, k -> 1L << indexCounter++);
	}

	/**
	 * Checks if a subset of this signature matches {@code other}.
	 * @param other signature to check against
	 * @return {@code true} if this signature contains all component types specified by {@code other}
	 */
	public final boolean masks(Signature other) {
		return (signature & other.signature) == other.signature;
	}

	/**
	 * Adds a component type to this signature.
	 * @param type added component type
	 */
	public void add(Class<? extends Component> type) {
		types.add(type);
		signature |= maskOf(type);
	}
	/**
	 * Removes a component type from this signature.
	 * @param type removed component type
	 */
	public void remove(Class<? extends Component> type) {
		types.remove(type);
		signature &= ~maskOf(type);
	}

	/** @return number of types composing this signature */
	public int size() {
		return types.size();
	}

	/** @return types composing this signature */
	public Collection<Class<? extends Component>> getTypes() {
		return types;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Signature other = (Signature) o;
		return signature == other.signature;
	}
	@Override
	public int hashCode() {
		return Objects.hash(signature);
	}

	@Override
	public int compareTo(Signature o) {
		return Long.compare(signature, o.signature);
	}
}
