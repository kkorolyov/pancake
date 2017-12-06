package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Animation;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.Signature;

import java.util.HashSet;
import java.util.Set;

public class AnimationSystem extends GameSystem {
	private final Set<Animation> tickedAnimations = new HashSet<>();

	/**
	 * Constructs a new animation system.
	 */
	public AnimationSystem() {
		super(new Signature(Animation.class));
	}

	@Override
	public void update(Entity entity, float dt) {
		Animation animation = entity.get(Animation.class);

		if (!tickedAnimations.contains(animation)) {
			animation.tick(dt);
			tickedAnimations.add(animation);
		}
	}
	@Override
	public void after(float dt) {
		tickedAnimations.clear();
	}
}
