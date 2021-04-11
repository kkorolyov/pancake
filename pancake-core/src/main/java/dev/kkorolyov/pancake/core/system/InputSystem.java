package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.ActionQueue;
import dev.kkorolyov.pancake.core.component.Input;
import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.core.input.Handler;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.Resources;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.Signature;
import dev.kkorolyov.pancake.platform.math.Vector2;
import dev.kkorolyov.pancake.platform.math.Vectors;
import dev.kkorolyov.pancake.platform.utility.Limiter;

/**
 * Applies actions using current player input.
 */
public class InputSystem extends GameSystem {
	private final Vector2 transformToCursor = Vectors.create(0, 0);  // TODO What to do with this?

	/**
	 * Constructs a new input system.
	 */
	public InputSystem() {
		super(
				new Signature(Input.class, ActionQueue.class),
				Limiter.fromConfig(InputSystem.class)
		);
	}

	@Override
	public void update(Entity entity, long dt) {
		Input input = entity.get(Input.class);
		Transform transform = entity.get(Transform.class);

		for (Handler handler : input.getHandlers()) {
			entity.get(ActionQueue.class).enqueue(handler.arm(Resources.APPLICATION.getInputs(), dt));
		}
		if (input.facesCursor() && transform != null) {
			transformToCursor.set(Resources.RENDER_MEDIUM.getCamera().getAbsolutePosition(Resources.APPLICATION.getCursor()));
			transformToCursor.add(transform.getPosition(), -1);

			transform.getOrientation().setX(Math.atan2(transformToCursor.getY(), transformToCursor.getX()));
		}
	}
}
