package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.utility.ArgVerify;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Maintains positional constraints to other entities.
 */
public final class Joint implements Component, Iterable<Map.Entry<Entity, Joint.Constraint>> {
	private static final ThreadLocal<Vector3> tMtv = ThreadLocal.withInitial(Vector3::of);

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
		 * Repositions joints selected by {@code targeter} to be within {@code min} and {@code max} (inclusive) Euclidean distance of each other.
		 * If {@code targeter} returns {@code < 0}, modifies the first joint; if {@code > 0}, modifies the second joint; if {@code 0}, modifies both joints equally in opposite directions.
		 * Assumes both joints have a {@link Transform}.
		 */
		static Constraint reposition(double min, double max, Comparator<Entity> targeter) {
			ArgVerify.greaterThanEqual("min", 0.0, min);
			ArgVerify.greaterThanEqual("max", min, max);

			return (a, b) -> {
				var mtv = tMtv.get();
				if (calculateMtv(mtv, a, b, min, max)) {
					var priority = targeter.compare(a, b);

					if (priority < 0) a.get(Transform.class).getTranslation().add(mtv, -1);
					else if (priority > 0) b.get(Transform.class).getTranslation().add(mtv);
					else {
						a.get(Transform.class).getTranslation().add(mtv, -0.5);
						b.get(Transform.class).getTranslation().add(mtv, 0.5);
					}
				}
			};
		}

		/**
		 * Adds temporary forces to joints selected by {@code targeter} to be within {@code min} and {@code max} (inclusive) Euclidean distance of each other.
		 * If {@code targeter} returns {@code < 0}, modifies the first joint; if {@code > 0}, modifies the second joint; if {@code 0}, modifies both joints equally in opposite directions.
		 * Force is calculated by scaling the found MTV by {@code strength} - e.g. if the joints are {@code 2} units further apart past the {@code max}, force added is {@code 2 * strength}.
		 * Assumes both joints have a {@link Transform}, {@link Force}, and {@link ActionQueue}.
		 */
		static Constraint force(double min, double max, double strength, Comparator<Entity> targeter) {
			ArgVerify.greaterThanEqual("min", 0.0, min);
			ArgVerify.greaterThanEqual("max", min, max);

			return (a, b) -> {
				var mtv = tMtv.get();
				if (calculateMtv(mtv, a, b, min, max)) {
					mtv.scale(strength);
					var force = Vector3.of(mtv);
					var priority = targeter.compare(a, b);

					if (priority < 0) {
						a.get(ActionQueue.class).add(entity -> {
							entity.get(Force.class).getValue().add(force, -1);
							entity.get(ActionQueue.class).add(entity1 -> entity1.get(Force.class).getValue().add(force));
						});
					} else if (priority > 0) {
						b.get(ActionQueue.class).add(entity -> {
							entity.get(Force.class).getValue().add(force);
							entity.get(ActionQueue.class).add(entity1 -> entity1.get(Force.class).getValue().add(force, -1));
						});
					} else {
						a.get(ActionQueue.class).add(entity -> {
							entity.get(Force.class).getValue().add(force, -0.5);
							entity.get(ActionQueue.class).add(entity1 -> entity1.get(Force.class).getValue().add(force, 0.5));
						});
						b.get(ActionQueue.class).add(entity -> {
							entity.get(Force.class).getValue().add(force, 0.5);
							entity.get(ActionQueue.class).add(entity1 -> entity1.get(Force.class).getValue().add(force, -0.5));
						});
					}
				}
			};
		}

		/**
		 * If joints {@code a} and {@code b} are not between {@code min} and {@code max} Euclidean distance of each other, sets {@code mtv} to the MTV to apply to {@code b} to remediate the positions and returns {@code true}.
		 * Else, returns {@code false}.
		 * Assumes both joints have a {@link Transform}.
		 */
		private static boolean calculateMtv(Vector3 mtv, Entity a, Entity b, double min, double max) {
			mtv.set(a.get(Transform.class).getTranslation());
			mtv.add(b.get(Transform.class).getTranslation(), -1);

			var distance = Vector3.magnitude(mtv);

			if (distance < min || distance > max) {
				var offset = (distance - (distance < min ? min : max)) / distance;
				// mtv to apply to B to remediate
				mtv.scale(offset);

				return true;
			} else {
				return false;
			}
		}

		/**
		 * Checks if joints {@code a} and {@code b} adhere to this constraint.
		 * If they do not, applies some remediation operation to the joints to fix.
		 * {@code a} is the entity owning the invoked {@link Joint} component, and {@code b} is the constrained joint.
		 */
		void apply(Entity a, Entity b);
	}
}
