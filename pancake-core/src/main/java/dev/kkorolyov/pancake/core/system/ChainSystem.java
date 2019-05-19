package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Chain;
import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.Signature;
import dev.kkorolyov.pancake.platform.math.Vector;
import dev.kkorolyov.pancake.platform.utility.Limiter;

import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Repositions chained entities.
 */
public class ChainSystem extends GameSystem {
	private final Vector transformToAnchor = new Vector();
	private final NavigableMap<Float, Vector> sortedAnchors = new TreeMap<>();

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
		Vector anchor = chain.getPositionAnchor();
		if (anchor == null) return;

		transformToAnchor.set(anchor);
		transformToAnchor.sub(transform.getPosition());

		float gap = transformToAnchor.getMagnitude() - chain.getPlay();
		if (gap > 0) {
			transformToAnchor.normalize();
			transformToAnchor.scale(gap);

			transform.getPosition().add(transformToAnchor);
		}
	}
	private void updateRotationAnchors(Chain chain, Transform transform) {
		for (Vector anchor : chain.getRotationAnchors()) {
			sortedAnchors.put(findDistance(transform.getPosition(), anchor), anchor);
		}
		if (sortedAnchors.size() > 0) {
			transformToAnchor.set(sortedAnchors.firstEntry().getValue());
			transformToAnchor.sub(transform.getPosition());

			transform.getOrientation().set(transformToAnchor.getPhi(), 0, transformToAnchor.getTheta());
		}
		sortedAnchors.clear();
	}
	private float findDistance(Vector parent, Vector child) {
		transformToAnchor.set(child);
		transformToAnchor.sub(parent);

		return transformToAnchor.getMagnitude();
	}
}
