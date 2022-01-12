package dev.kkorolyov.pancake.platform.entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toSet;

/**
 * A distinct combination of registered component types.
 */
public final class Signature implements Comparable<Signature> {
	private static final HashMap<Class<? extends Component>, Byte> INDEX_MAP = new HashMap<>();

	private final Collection<Class<? extends Component>> types;
	private final long signature;

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
		this.types = StreamSupport.stream(types.spliterator(), false)
				.collect(toSet());
		signature = this.types.stream()
				.map(Signature::maskOf)
				.reduce(0L, (sig, i) -> sig | i);
	}
	private static long maskOf(Class<? extends Component> type) {
		return 1L << INDEX_MAP.computeIfAbsent(type, k -> (byte) INDEX_MAP.size());
	}

	/**
	 * Checks if a subset of this signature matches {@code other}.
	 * @param other signature to check against
	 * @return {@code true} if this signature contains all component types specified by {@code other}
	 */
	public boolean masks(Signature other) {
		return (signature & other.signature) == other.signature;
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
	public int compareTo(Signature o) {
		return Long.compare(signature, o.signature);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Signature o = (Signature) obj;
		return signature == o.signature;
	}
	@Override
	public int hashCode() {
		return Objects.hash(signature);
	}

	@Override
	public String toString() {
		return "Signature{" +
				"types=" + types +
				", signature=" + signature +
				'}';
	}
}
