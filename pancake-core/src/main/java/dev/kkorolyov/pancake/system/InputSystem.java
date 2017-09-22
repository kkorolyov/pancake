package dev.kkorolyov.pancake.system;

import dev.kkorolyov.pancake.GameSystem;
import dev.kkorolyov.pancake.component.Input;
import dev.kkorolyov.pancake.component.Transform;
import dev.kkorolyov.pancake.entity.Entity;
import dev.kkorolyov.pancake.entity.Signature;
import dev.kkorolyov.pancake.graphics.Camera;
import dev.kkorolyov.pancake.input.KeyAction;
import dev.kkorolyov.pancake.math.Vector;

import javafx.scene.Scene;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static dev.kkorolyov.pancake.event.PlatformEvents.CAMERA_CREATED;
import static dev.kkorolyov.pancake.event.PlatformEvents.SCENE_CREATED;

/**
 * Applies actions using current player input.
 */
public class InputSystem extends GameSystem {
	private final Set<Enum<?>> pressedKeys = new HashSet<>();
	private final Vector relCursor = new Vector();
	private final Vector cursor = new Vector();	// TODO What to do with this?
	private Camera camera;

	/**
	 * Constructs a new input system.
	 */
	public InputSystem() {
		super(new Signature(Input.class));
	}
	@Override
	public void attach() {
		register(SCENE_CREATED, (Scene scene) -> {
			scene.setOnMouseMoved(e -> relCursor.set((float) e.getX(), (float) e.getY()));

			scene.setOnMousePressed((e) -> pressedKeys.add(e.getButton()));
			scene.setOnMouseReleased((e) -> pressedKeys.remove(e.getButton()));

			scene.setOnKeyPressed(e -> pressedKeys.add(e.getCode()));
			scene.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));
		});
		register(CAMERA_CREATED, (Camera camera) -> this.camera = camera);
	}

	@Override
	public void update(Entity entity, float dt) {
		for (KeyAction keyAction : entity.get(Input.class).getActions()) {
			Consumer<? super Entity> action = keyAction.signal(pressedKeys, dt);
			if (action != null) action.accept(entity);
		}
		Transform transform = entity.get(Transform.class);
		if (entity.get(Input.class).facesCursor() && transform != null) {
			cursor.set(camera.getAbsolutePosition(relCursor));
			cursor.sub(transform.getPosition());

			transform.setRotation((float) (-cursor.getTheta() * 180 / Math.PI + 90));
		}
	}
}
