package dev.kkorolyov.pancake.system;

import dev.kkorolyov.pancake.GameSystem;
import dev.kkorolyov.pancake.component.Chain;
import dev.kkorolyov.pancake.component.Transform;
import dev.kkorolyov.pancake.entity.Entity;
import dev.kkorolyov.pancake.entity.Signature;
import dev.kkorolyov.pancake.math.Vector;

import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Repositions chained entities.
 */
public class ChainSystem extends GameSystem {
	private final Vector distance = new Vector();
	private final NavigableMap<Float, Vector> sortedAnchors = new TreeMap<>();

	/**
	 * Constructs a new chain system.
	 */
	public ChainSystem() {
		super(new Signature(Transform.class,
												Chain.class));
	}

	@Override
	public void update(Entity entity, float dt) {
		Chain chain = entity.get(Chain.class);
		Transform transform = entity.get(Transform.class);

		updatePositionAnchor(chain, transform);
		updateRotationAnchors(chain, transform);
	}
	private void updatePositionAnchor(Chain chain, Transform transform) {
		Vector anchor = chain.getPositionAnchor();
		if (anchor == null) return;

		distance.set(anchor);
		distance.sub(transform.getPosition());

		float gap = distance.getMagnitude() - chain.getPlay();
		if (gap > 0) {
			distance.normalize();
			distance.scale(gap);

			transform.getPosition().add(distance);
		}
	}
	private void updateRotationAnchors(Chain chain, Transform transform) {
		for (Vector anchor : chain.getRotationAnchors()) {
			sortedAnchors.put(findDistance(transform.getPosition(), anchor), anchor);
		}
		if (sortedAnchors.size() > 0) {
			distance.set(sortedAnchors.firstEntry().getValue());
			distance.sub(transform.getPosition());

			transform.setRotation((float) (-distance.getTheta() * 180 / Math.PI + 90));
		}
	}
	private float findDistance(Vector parent, Vector child) {
		distance.set(child);
		distance.sub(parent);

		return distance.getMagnitude();
	}
}
