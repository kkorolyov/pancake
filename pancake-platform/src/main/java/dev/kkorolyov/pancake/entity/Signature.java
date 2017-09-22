package dev.kkorolyov.pancake.entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

/**
 * A distinct combination of registered component types.
 */
public class Signature {
	private static final HashMap<Class<? extends Component>, Long> indexMap = new HashMap<>();

	/**
	 * Sets the collection of component types used in masking to all {@link Component} types provided in a service file.
	 * @throws IllegalArgumentException if a non-concrete type is indexed
	 */
	public static void index() {
		index(loadComponentClasses());
	}
	/**
	 * Sets the collection of component types used in masking.
	 * @param types indexed types
	 * @throws IllegalArgumentException if a non-concrete type is indexed
	 */
	@SafeVarargs
	public static void index(Class<? extends Component>... types) {
		index(Arrays.asList(types));
	}
	/**
	 * Sets the collection of component types used in masking.
	 * @param types indexed types
	 * @throws IllegalArgumentException if a non-concrete type is indexed
	 */
	public static void index(Iterable<Class<? extends Component>> types) {
		indexMap.clear();

		long counter = 0;
		if (types != null) {
			for (Class<? extends Component> type : types) {
				if (type.isInterface() || Modifier.isAbstract(type.getModifiers()))	{
					throw new IllegalArgumentException(type + " is not a concrete type");
				}
				indexMap.put(type, counter++);
			}
		}
	}

	private static Iterable<Class<? extends Component>> loadComponentClasses() {
		Collection<Class<? extends Component>> componentClasses = new ArrayList<>();

		try {
			for (URL resource : Collections.list(ClassLoader.getSystemResources(
					"META-INF/services/" + Component.class.getName()))) {
				try (BufferedReader in = new BufferedReader(new InputStreamReader(
						resource.openStream(), Charset.forName("UTF-8")))) {
					in.lines()
							.map(Signature::toClass)
							.forEach(componentClasses::add);
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return componentClasses;
	}
	private static Class<? extends Component> toClass(String className) {
		try {
			return Class.forName(className).asSubclass(Component.class);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
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
