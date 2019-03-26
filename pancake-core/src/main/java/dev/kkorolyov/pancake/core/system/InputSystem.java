package dev.kkorolyov.pancake.core.system;

import dev.kkorolyov.pancake.core.component.ActionQueue;
import dev.kkorolyov.pancake.core.component.Input;
import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.platform.GameSystem;
import dev.kkorolyov.pancake.platform.action.KeyAction;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.Signature;
import dev.kkorolyov.pancake.platform.event.CameraCreated;
import dev.kkorolyov.pancake.platform.event.SceneCreated;
import dev.kkorolyov.pancake.platform.math.Vector;
import dev.kkorolyov.pancake.platform.media.Camera;
import dev.kkorolyov.pancake.platform.utility.Limiter;

import javafx.scene.Scene;
import java.util.HashSet;
import java.util.Set;

/**
 * Applies actions using current player input.
 */
public class InputSystem extends GameSystem {
	private final Set<Enum> pressedKeys = new HashSet<>();
	private final Vector relCursor = new Vector();
	private final Vector transformToCursor = new Vector();  // TODO What to do with this?
	private Camera camera;

	/**
	 * Constructs a new input system.
	 */
	public InputSystem() {
		super(
				new Signature(Input.class, ActionQueue.class),
				new Limiter(0)
		);
	}
	@Override
	public void attach() {
		resources.events
				.register(SceneCreated.class, se -> {
					Scene scene = se.getScene();

					scene.setOnMouseMoved(e -> relCursor.set((float) e.getX(), (float) e.getY()));

					scene.setOnMousePressed(e -> pressedKeys.add(e.getButton()));
					scene.setOnMouseReleased(e -> pressedKeys.remove(e.getButton()));

					scene.setOnKeyPressed(e -> pressedKeys.add(e.getCode()));
					scene.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));
				})
				.register(CameraCreated.class, e -> camera = e.getCamera());
	}

	@Override
	public void update(Entity entity, long dt) {
		Input input = entity.get(Input.class);
		Transform transform = entity.get(Transform.class);

		for (KeyAction keyAction : input.getActions()) {
			// TODO Remove seconds conversion
			entity.get(ActionQueue.class).enqueue(keyAction.arm(pressedKeys, dt / 1e9f));
		}
		if (input.facesCursor() && transform != null) {
			transformToCursor.set(camera.getAbsolutePosition(relCursor));
			transformToCursor.sub(transform.getPosition());

			transform.getOrientation().set(transformToCursor.getPhi(), 0, transformToCursor.getTheta());
		}
	}
}
