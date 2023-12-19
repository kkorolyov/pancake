package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.utility.ArgVerify;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Maintains positional constraints to other entities.
 */
public final class Joint implements Component, Iterable<Map.Entry<Entity, Joint.Constraint>> {
	private final Map<Entity, Constraint> connections = new HashMap<>();

	/**
	 * Sets a connection between the owning entity and {@code entity} according to {@code constraint}.
	 * Applies all constraints functions using {@code entity} as {@code a} and the constrained joint as {@code b}.
	 */
	public void put(Entity entity, Constraint constraint) {
		connections.put(entity, constraint);
	}

	/**
	 * Returns an iterator over all connections.
	 */
	@Override
	public Iterator<Map.Entry<Entity, Constraint>> iterator() {
		return connections.entrySet().iterator();
	}

	/**
	 * Ensures that 2 joints maintain some specific relation to each other.
	 */
	@FunctionalInterface
	public interface Constraint {
		/**
		 * Repositions joints to be within {@code min} and {@code max} (inclusive) Euclidean distance of each other.
		 * Repositions those joints selected by {@code targeter}.
		 * Assumes both joints have a {@link Position}.
		 */
		static Constraint reposition(double min, double max, BiFunction<? super Entity, ? super Entity, Target> targeter) {
			ArgVerify.greaterThanEqual("min", 0.0, min);
			ArgVerify.greaterThanEqual("max", min, max);

			ThreadLocal<Vector3> tMtv = ThreadLocal.withInitial(Vector3::of);

			return (a, b) -> {
				Vector3 aPosition = a.get(Position.class).getValue();
				Vector3 bPosition = b.get(Position.class).getValue();

				var mtv = tMtv.get();
				mtv.set(aPosition);
				mtv.add(bPosition, -1);

				var distance = Vector3.magnitude(mtv);

				if (distance < min || distance > max) {
					var offset = (distance - (distance < min ? min : max)) / distance;
					// mtv to apply to B to remediate
					mtv.scale(offset);

					switch (targeter.apply(a, b)) {
						case A -> aPosition.add(mtv, -1);
						case B -> bPosition.add(mtv);
						case BOTH -> {
							aPosition.add(mtv, -0.5);
							bPosition.add(mtv, 0.5);
						}
					}
				}
			};
		}

		/**
		 * Checks if joints {@code a} and {@code b} adhere to this constraint.
		 * If they do not, applies some remediation operation to the joints to fix.
		 * {@code a} is the entity owning the invoked {@link Joint} component, and {@code b} is the constrained joint.
		 */
		void apply(Entity a, Entity b);

		/**
		 * The target joint to modify in a remediation operation.
		 */
		enum Target {
			/**
			 * Modify only joint A.
			 */
			A,
			/**
			 * Modify only joint B.
			 */
			B,
			/**
			 * Modify both joints equally.
			 */
			BOTH
		}
	}
}
