package dev.kkorolyov.pancake;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import dev.kkorolyov.pancake.component.*;

/**
 * A distinct combination of registered component types.
 */
public class Signature {
	private static final HashMap<Class<? extends Component>, Long> indexMap = new HashMap<>();
	private static final List<Class<? extends Component>> defaultTypes = Arrays.asList(Bounds.class,
																																										 Damping.class,
																																										 Force.class,
																																										 MaxSpeed.class,
																																										 Sprite.class,
																																										 Transform.class,
																																										 Velocity.class);

	static {
		index();
	}

	/**
	 * Sets the collection of additional component types used in masking.
	 * @param types indexed types, if {@code null} or empty, only the default component types are used
	 */
	@SafeVarargs
	public static void index(Class<? extends Component>... types) {
		indexMap.clear();

		long counter = 0;
		for (Class<? extends Component> type : defaultTypes) {
			indexMap.put(type, counter++);
		}
		if (types != null) {
			for (Class<? extends Component> type : types) {
				indexMap.put(type, counter++);
			}
		}
	}

	private long signature;

	/**
	 * Constructs a new signature without any component types.
	 */
	public Signature() {
		this((Class<? extends Component>[]) null);
	}
	/**
	 * Constructs a new signature from a set of component types.
	 * @param types types deciding signature
	 */
	@SafeVarargs
	public Signature(Class<? extends Component>... types) {
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
	public final boolean contains(Signature other) {
		return (signature & other.signature) == other.signature;
	}
	/**
	 * Checks if this signature contains some set of component types.
	 * @param types queried component types
	 * @return {code true} if this signature contains all {@code types}
	 */
	@SafeVarargs
	public final boolean contains(Class<? extends Component>... types) {
		long mask = 0;
		for (Class<? extends Component> type : types) {
			mask |= getMask(type);
		}
		return (signature & mask) == mask;
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
		return 1 << indexMap.get(type);
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
