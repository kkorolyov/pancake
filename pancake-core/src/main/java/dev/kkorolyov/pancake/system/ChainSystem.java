package dev.kkorolyov.pancake.system;

import dev.kkorolyov.pancake.Entity;
import dev.kkorolyov.pancake.GameSystem;
import dev.kkorolyov.pancake.Signature;
import dev.kkorolyov.pancake.component.Chain;
import dev.kkorolyov.pancake.component.Transform;
import dev.kkorolyov.pancake.math.Vector;

/**
 * Repositions chained entities.
 */
public class ChainSystem extends GameSystem {
	private final Vector distance = new Vector();

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

		Vector origin = entity.get(Transform.class).getPosition();
		Vector anchorOrigin = chain.getAnchor().getPosition();
		float play = chain.getPlay();

		distance.set(anchorOrigin);
		distance.sub(origin);

		float gap = distance.getMagnitude() - play;
		if (gap > 0) {
			distance.normalize();
			distance.scale(gap);

			origin.add(distance);
		}
	}
}
