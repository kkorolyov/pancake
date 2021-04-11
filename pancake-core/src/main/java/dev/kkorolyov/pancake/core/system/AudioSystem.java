package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.AudioEmitter;
import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.platform.Config;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.Resources;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.Signature;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.math.VectorMath;
import dev.kkorolyov.pancake.platform.math.Vectors;
import dev.kkorolyov.pancake.platform.utility.Limiter;

/**
 * Starts and stops audio clips.
 */
public class AudioSystem extends GameSystem {
	private final Vector3 relativeEmitter = Vectors.create(0, 0, 0);

	/**
	 * Constructs a new sound system.
	 */
	public AudioSystem() {
		super(
				new Signature(AudioEmitter.class, Transform.class),
				Limiter.fromConfig(AudioSystem.class)
		);
	}

	@Override
	public void update(Entity entity, long dt) {
		AudioEmitter emitter = entity.get(AudioEmitter.class);
		Vector3 position = entity.get(Transform.class).getGlobalPosition();

		relativeEmitter.set(position);
		relativeEmitter.add(Resources.RENDER_MEDIUM.getCamera().getPosition(), -1);

		double centralRadius = Double.parseDouble(Config.get(getClass()).getProperty("centralRadius"));

		emitter.apply(
				centralRadius / VectorMath.magnitude(relativeEmitter),
				relativeEmitter.getX() / centralRadius
		);
	}
}
