package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Bounds;
import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.core.component.event.Intersected;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.math.Vector2;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.math.Vectors;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;

/**
 * Detects entity intersections and enqueues intersection events with normalized minimum translation vectors.
 */
public final class IntersectionSystem extends GameSystem {
	private final Queue<Entity> toCheck = new ArrayDeque<>();

	private double minOverlap;
	private final Vector2 mtv = Vectors.create(0, 0);

	private final Collection<Vector2> seenAxes = new ArrayList<>();
	private final Projection aProj = new Projection(), bProj = new Projection();

	/**
	 * Constructs a new intersection system.
	 */
	public IntersectionSystem() {
		super(Transform.class, Bounds.class);
	}

	@Override
	public void update(Entity entity, long dt) {
		toCheck.add(entity);
	}
	@Override
	public void after() {
		for (Entity a = toCheck.poll(); a != null; a = toCheck.poll()) {
			for (Entity b : toCheck) {
				process(a, b);
			}
		}
		toCheck.clear();
	}
	private void process(Entity a, Entity b) {
		Transform aTransform = a.get(Transform.class);
		Transform bTransform = b.get(Transform.class);

		Bounds aBounds = a.get(Bounds.class);
		Bounds bBounds = b.get(Bounds.class);

		// polygons can be far enough apart to not need a more precise check
		// only consider when either can be corrected
		if ((aBounds.isCorrectable() || bBounds.isCorrectable()) && isClose(aTransform, bTransform, aBounds, bBounds)) {
			if (aBounds.isRound() && bBounds.isRound()) processRound(aTransform, bTransform, aBounds, bBounds);
			else processPoly(aTransform, bTransform, aBounds, bBounds);

			if (mtv.getX() != 0 || mtv.getY() != 0) {
				a.put(new Intersected(b, mtv));

				mtv.scale(minOverlap);

				// move appropriate entity(s) to remove overlap
				if (aBounds.isCorrectable()) {
					if (bBounds.isCorrectable()) {
						aTransform.getPosition().add(mtv, 0.5);
						bTransform.getPosition().add(mtv, -0.5);
					} else {
						aTransform.getPosition().add(mtv);
					}
				} else {
					bTransform.getPosition().add(mtv, -1);
				}
			}
			minOverlap = 0;
			mtv.setX(0);
			mtv.setY(0);
		}
	}
	private static boolean isClose(Transform aTransform, Transform bTransform, Bounds aBounds, Bounds bBounds) {
		return aBounds.getMagnitude() + bBounds.getMagnitude() > Vector3.distance(aTransform.getPosition(), bTransform.getPosition());
	}

	private void processRound(Transform aTransform, Transform bTransform, Bounds aBounds, Bounds bBounds) {
		double midDistance = Vector2.distance(aTransform.getPosition(), bTransform.getPosition());
		double overlap = aBounds.getMagnitude() + bBounds.getMagnitude() - midDistance;

		if (overlap > 0) {
			minOverlap = overlap;

			if (midDistance != 0) {
				mtv.set(aTransform.getPosition());
				mtv.add(bTransform.getPosition(), -1);
				mtv.scale(1 / midDistance);
			} else {
				// same position, just shift along x-axis
				mtv.setX(-1);
			}
		}
	}
	private void processPoly(Transform aTransform, Transform bTransform, Bounds aBounds, Bounds bBounds) {
		for (Vector2 axis : aBounds.getNormals()) {
			if (!processAxis(axis, aTransform, bTransform, aBounds, bBounds)) {
				minOverlap = 0;
				mtv.setX(0);
				mtv.setY(0);
				seenAxes.clear();
				return;
			}
		}
		for (Vector2 axis : bBounds.getNormals()) {
			if (!processAxis(axis, aTransform, bTransform, aBounds, bBounds)) {
				minOverlap = 0;
				mtv.setX(0);
				mtv.setY(0);
				seenAxes.clear();
				return;
			}
		}
		seenAxes.clear();
	}

	private boolean processAxis(Vector2 axis, Transform aTransform, Transform bTransform, Bounds aBounds, Bounds bBounds) {
		for (Vector2 seen : seenAxes) {
			if (isSameAxis(seen, axis)) return true;
		}
		seenAxes.add(axis);

		project(aTransform, aBounds, axis, aProj);
		project(bTransform, bBounds, axis, bProj);

		double overlap = aProj.getOverlap(bProj);
		if (overlap == 0) {
			aProj.clear();
			bProj.clear();
			return false;
		} else if (minOverlap == 0 || Double.compare(Math.abs(overlap), Math.abs(minOverlap)) < 0) {
			minOverlap = overlap;
			// save current axis as MTV axis
			mtv.setX(axis.getX());
			mtv.setY(axis.getY());
		}
		aProj.clear();
		bProj.clear();
		return true;
	}
	private static boolean isSameAxis(Vector2 a, Vector2 b) {
		return (Double.compare(a.getX(), b.getX()) == 0 && Double.compare(a.getY(), b.getY()) == 0)
				|| (Double.compare(-a.getX(), b.getX()) == 0 && Double.compare(-a.getY(), b.getY()) == 0);
	}
	private static void project(Transform transform, Bounds bounds, Vector2 axis, Projection projection) {
		if (bounds.isRound()) {
			projection.put(bounds.getMagnitude());
			projection.put(-bounds.getMagnitude());
		} else {
			for (Vector3 vertex : bounds.getVertices()) {
				projection.put(Vector2.dot(axis, vertex));
			}
		}
		projection.translate(Vector2.dot(axis, transform.getPosition()));
	}

	private static final class Projection {
		private double min, max;

		void put(double val) {
			if (val < min) min = val;
			else if (val > max) max = val;
		}

		void translate(double val) {
			min += val;
			max += val;
		}

		// in terms of distance and direction on a line that this projection would move to no longer overlap with other
		double getOverlap(Projection other) {
			// all identical points
			if (min == max && min == other.min && min == other.max) return Double.MAX_VALUE;

			return max > other.min && other.max > min
					? max < other.max ? other.min - max : other.max - min
					: 0;
		}

		void clear() {
			min = 0;
			max = 0;
		}
	}
}
