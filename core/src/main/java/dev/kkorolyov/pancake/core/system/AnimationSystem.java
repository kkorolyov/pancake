package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.AnimationQueue;
import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Applies {@link AnimationQueue} animations to {@link Transform}s.
 */
public final class AnimationSystem extends GameSystem {
	private final Map<AnimationQueue, Collection<Transform>> animationQueues = new HashMap<>();

	public AnimationSystem() {
		super(Transform.class, AnimationQueue.class);
	}

	@Override
	protected void update(Entity entity, long dt) {
		animationQueues.computeIfAbsent(entity.get(AnimationQueue.class), k -> new HashSet<>()).add(entity.get(Transform.class));
	}

	@Override
	protected void after(long dt) {
		animationQueues.forEach((animationQueue, transforms) -> {
			var frame = animationQueue.update(dt);
			if (frame != null) {
				for (Transform transform : transforms) {
					frame.apply(transform);
				}
			}
		});
	}
}
