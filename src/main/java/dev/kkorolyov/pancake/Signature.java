package dev.kkorolyov.pancake;

import java.util.HashMap;
import java.util.Objects;

import dev.kkorolyov.pancake.component.*;

/**
 * A distinct combination of registered component types.
 */
public class Signature {
	private static final HashMap<Class<? extends Component>, Long> indexMap = new HashMap<>();
	static {
		index(Bounds.class,
					Damping.class,
					Force.class,
					MaxSpeed.class,
					Sprite.class,
					Transform.class,
					Velocity.class);
	}

	/**
	 * Sets the collection of component types used in masking.
	 * @param types indexed types
	 */
	@SafeVarargs
	public static void index(Class<? extends Component>... types) {
		indexMap.clear();

		long counter = 0;
		for (Class<? extends Component> type : types) {
			indexMap.put(type, counter++);
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
	 * Checks if this signature has some set of component types.
	 * @param types queried component types
	 * @return {code true} if this signature has all {@code types}
	 */
	@SafeVarargs
	public final boolean has(Class<? extends Component>... types) {
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
