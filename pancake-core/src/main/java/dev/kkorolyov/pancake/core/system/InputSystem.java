package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.ActionQueue;
import dev.kkorolyov.pancake.core.component.Input;
import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.Resources;
import dev.kkorolyov.pancake.platform.action.KeyAction;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.Signature;
import dev.kkorolyov.pancake.platform.math.Vector;
import dev.kkorolyov.pancake.platform.utility.Limiter;

/**
 * Applies actions using current player input.
 */
public class InputSystem extends GameSystem {
	private final Vector transformToCursor = new Vector();  // TODO What to do with this?

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

		for (KeyAction keyAction : input.getActions()) {
			entity.get(ActionQueue.class).enqueue(keyAction.arm(Resources.APPLICATION.getInputs(), dt));
		}
		if (input.facesCursor() && transform != null) {
			transformToCursor.set(Resources.RENDER_MEDIUM.getCamera().getAbsolutePosition(Resources.APPLICATION.getCursor()));
			transformToCursor.sub(transform.getPosition());

			transform.getOrientation().set(transformToCursor.getPhi(), 0, transformToCursor.getTheta());
		}
	}
}
