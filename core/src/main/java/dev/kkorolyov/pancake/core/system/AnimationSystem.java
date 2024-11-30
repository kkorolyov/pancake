package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.Animator;
import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Applies {@link Animator} animations to {@link Transform}s.
 */
public final class AnimationSystem extends GameSystem {
	private final Map<Animator, Collection<Transform>> animators = new IdentityHashMap<>();

	public AnimationSystem() {
		super(Transform.class, Animator.class);
	}

	@Override
	protected void update(Entity entity, long dt) {
		animators.computeIfAbsent(entity.get(Animator.class), k -> new HashSet<>()).add(entity.get(Transform.class));
	}

	@Override
	protected void after(long dt) {
		animators.forEach((animator, transforms) -> {
			var frame = animator.update(dt);
			if (frame != null) {
				for (Transform transform : transforms) {
					frame.apply(transform);
				}
			}
		});

		animators.clear();
	}
}
