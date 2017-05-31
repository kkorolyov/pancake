package dev.kkorolyov.pancake.core;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import dev.kkorolyov.pancake.component.Sprite;
import dev.kkorolyov.pancake.component.Transform;
import dev.kkorolyov.pancake.component.collision.EllipseBounds;
import dev.kkorolyov.pancake.component.collision.RectangleBounds;
import dev.kkorolyov.pancake.component.movement.Damping;
import dev.kkorolyov.pancake.component.movement.Force;
import dev.kkorolyov.pancake.component.movement.MaxSpeed;
import dev.kkorolyov.pancake.component.movement.Velocity;
import dev.kkorolyov.pancake.component.control.InteractiveEntityController;

/**
 * A distinct combination of registered component types.
 */
public class Signature {
	private static final HashMap<Class<? extends Component>, Long> indexMap = new HashMap<>();
	private static final List<Class<? extends Component>> coreTypes = Arrays.asList(RectangleBounds.class,
																																									EllipseBounds.class,
																																									Damping.class,
																																									Force.class,
																																									MaxSpeed.class,
																																									Sprite.class,
																																									Transform.class,
																																									Velocity.class,
																																									InteractiveEntityController.class);

	static {
		index();
	}

	/**
	 * Sets the collection of additional component types used in masking.
	 * @param types indexed types, if {@code null} or empty, only the core component types are used
	 * @throws IllegalArgumentException if a non-concrete type is indexed
	 */
	@SafeVarargs
	public static void index(Class<? extends Component>... types) {
		index(Arrays.asList(types));
	}
	/**
	 * Sets the collection of additional component types used in masking.
	 * @param types indexed types, if {@code null} or empty, only the default component types are used
	 * @throws IllegalArgumentException if a non-concrete type is indexed
	 */
	public static void index(Iterable<Class<? extends Component>> types) {
		indexMap.clear();

		long counter = 0;
		for (Class<? extends Component> type : coreTypes) {
			if (type.isInterface() || Modifier.isAbstract(type.getModifiers()))	{
				throw new IllegalArgumentException(type + " is not a concrete type");
			}
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
		this((Iterable<Class<? extends Component>>) null);
	}
	/**
	 * Constructs a new signature from a set of component types.
	 * @param types types defining signature
	 */
	@SafeVarargs
	public Signature(Class<? extends Component>... types) {
		Arrays.asList(types);
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
