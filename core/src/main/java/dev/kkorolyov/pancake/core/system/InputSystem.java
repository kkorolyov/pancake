package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.ActionQueue;
import dev.kkorolyov.pancake.core.component.Input;
import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.core.input.Handler;
import dev.kkorolyov.pancake.platform.plugin.GameSystem;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.Signature;
import dev.kkorolyov.pancake.platform.math.Vector2;
import dev.kkorolyov.pancake.platform.math.Vectors;
import dev.kkorolyov.pancake.platform.plugin.Application;
import dev.kkorolyov.pancake.platform.plugin.Plugins;
import dev.kkorolyov.pancake.platform.plugin.RenderMedium;
import dev.kkorolyov.pancake.platform.utility.Limiter;

/**
 * Applies actions using current player input.
 */
public class InputSystem extends GameSystem {
	private final Application application;
	private final RenderMedium renderMedium;

	private final Vector2 transformToCursor = Vectors.create(0, 0);  // TODO What to do with this?

	/**
	 * Constructs a new input system.
	 */
	public InputSystem() {
		this(Plugins.application(), Plugins.renderMedium());
	}
	InputSystem(Application application, RenderMedium renderMedium) {
		super(
				new Signature(Input.class, ActionQueue.class),
				Limiter.fromConfig(InputSystem.class)
		);
		this.application = application;
		this.renderMedium = renderMedium;
	}

	@Override
	public void update(Entity entity, long dt) {
		Input input = entity.get(Input.class);
		Transform transform = entity.get(Transform.class);

		for (Handler handler : input.getHandlers()) {
			entity.get(ActionQueue.class).enqueue(handler.arm(application.getInputs(), dt));
		}
		if (input.facesCursor() && transform != null) {
			transformToCursor.set(renderMedium.getCamera().getAbsolutePosition(application.getCursor()));
			transformToCursor.add(transform.getPosition(), -1);

			transform.getOrientation().setX(Math.atan2(transformToCursor.getY(), transformToCursor.getX()));
		}
	}
}
