package dev.kkorolyov.pancake.platform.entity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 * A distinct combination of registered component types.
 */
public class Signature {
	private static final HashMap<Class<? extends Component>, Long> indexMap = new HashMap<>();
	private static long indexCounter;

	private static long indexOf(Class<? extends Component> type) {
		return indexMap.computeIfAbsent(type, k -> indexCounter++);
	}

	private long signature;

	/**
	 * Constructs a new signature without any component types.
	 */
	public Signature() {
		this((Iterable<Class<? extends Component>>) null);
	}
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
		signature |= getMask(type);
	}
	/**
	 * Removes a component type from this signature.
	 * @param type removed component type
	 */
	public void remove(Class<? extends Component> type) {
		signature &= ~getMask(type);
	}

	/** @return mask consisting of a single 1 bit in {@code type's} bit index */
	private long getMask(Class<? extends Component> type) {
		return 1 << indexOf(type);
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
}
