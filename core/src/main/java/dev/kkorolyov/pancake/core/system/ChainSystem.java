package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Chain;
import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.platform.plugin.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.Signature;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.math.VectorMath;
import dev.kkorolyov.pancake.platform.math.Vectors;
import dev.kkorolyov.pancake.platform.utility.Limiter;

import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Repositions chained entities.
 */
public class ChainSystem extends GameSystem {
	private final Vector3 transformToAnchor = Vectors.create(0, 0, 0);
	private final NavigableMap<Double, Vector3> sortedAnchors = new TreeMap<>();

	/**
	 * Constructs a new chain system.
	 */
	public ChainSystem() {
		super(
				new Signature(Transform.class, Chain.class),
				Limiter.fromConfig(ChainSystem.class)
		);
	}

	@Override
	public void update(Entity entity, long dt) {
		Chain chain = entity.get(Chain.class);
		Transform transform = entity.get(Transform.class);

		updatePositionAnchor(chain, transform);
		updateRotationAnchors(chain, transform);
	}
	private void updatePositionAnchor(Chain chain, Transform transform) {
		Vector3 anchor = chain.getPositionAnchor();
		if (anchor == null) return;

		transformToAnchor.set(anchor);
		transformToAnchor.add(transform.getPosition(), -1);

		double gap = VectorMath.magnitude(transformToAnchor) - chain.getPlay();
		if (gap > 0) {
			transformToAnchor.scale(1 / VectorMath.magnitude(transformToAnchor));
			transformToAnchor.scale(gap);

			transform.getPosition().add(transformToAnchor);
		}
	}
	private void updateRotationAnchors(Chain chain, Transform transform) {
		for (Vector3 anchor : chain.getRotationAnchors()) {
			sortedAnchors.put(VectorMath.distance(transform.getPosition(), anchor), anchor);
		}
		if (sortedAnchors.size() > 0) {
			transformToAnchor.set(sortedAnchors.firstEntry().getValue());
			transformToAnchor.add(transform.getPosition(), -1);

			transform.getOrientation().setX(Math.atan2(transformToAnchor.getY(), transformToAnchor.getX()));
		}
		sortedAnchors.clear();
	}
}
