package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Chain;
import dev.kkorolyov.pancake.core.component.Orientation;
import dev.kkorolyov.pancake.core.component.Position;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.math.Vector3;

import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Repositions chained entities.
 */
public class ChainSystem extends GameSystem {
	private final Vector3 positionToAnchor = Vector3.of(0, 0, 0);
	private final NavigableMap<Double, Vector3> sortedAnchors = new TreeMap<>();

	/**
	 * Constructs a new chain system.
	 */
	public ChainSystem() {
		super(Position.class, Chain.class);
	}

	@Override
	public void update(Entity entity, long dt) {
		Chain chain = entity.get(Chain.class);
		Position position = entity.get(Position.class);
		Orientation orientation = entity.get(Orientation.class);

		updatePositionAnchor(chain, position);
		if (orientation != null) updateRotationAnchors(chain, position, orientation);
	}
	private void updatePositionAnchor(Chain chain, Position position) {
		Vector3 anchor = chain.getPositionAnchor();
		if (anchor == null) return;

		positionToAnchor.set(anchor);
		positionToAnchor.add(position.getValue(), -1);

		double gap = Vector3.magnitude(positionToAnchor) - chain.getPlay();
		if (gap > 0) {
			positionToAnchor.scale(1 / Vector3.magnitude(positionToAnchor));
			positionToAnchor.scale(gap);

			position.getValue().add(positionToAnchor);
		}
	}
	private void updateRotationAnchors(Chain chain, Position position, Orientation orientation) {
		for (Vector3 anchor : chain.getRotationAnchors()) {
			sortedAnchors.put(Vector3.distance(position.getValue(), anchor), anchor);
		}
		if (sortedAnchors.size() > 0) {
			positionToAnchor.set(sortedAnchors.firstEntry().getValue());
			positionToAnchor.add(position.getValue(), -1);

			orientation.getValue().setX(Math.atan2(positionToAnchor.getY(), positionToAnchor.getX()));
		}
		sortedAnchors.clear();
	}
}
