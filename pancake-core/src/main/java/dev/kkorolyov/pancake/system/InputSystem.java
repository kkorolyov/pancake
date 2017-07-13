package dev.kkorolyov.pancake.system;

import dev.kkorolyov.pancake.GameSystem;
import dev.kkorolyov.pancake.component.Input;
import dev.kkorolyov.pancake.component.Transform;
import dev.kkorolyov.pancake.entity.Entity;
import dev.kkorolyov.pancake.entity.Signature;
import dev.kkorolyov.pancake.graphics.Camera;
import dev.kkorolyov.pancake.input.KeyAction;
import dev.kkorolyov.pancake.math.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import javafx.scene.Scene;

/**
 * Applies actions using current player input.
 */
public class InputSystem extends GameSystem {
	private final Set<Enum<?>> pressedKeys = new HashSet<>();
	private final Vector relCursor = new Vector();
	private final Vector cursor;
	private final Camera camera;

	/**
	 * Constructs a new input system.
	 * @param scene scene providing player input
	 * @param camera camera providing for mapping cursor position from render to absolute coordinates
	 * @param cursor vector kept updated with absolute cursor position
	 */
	public InputSystem(Scene scene, Camera camera, Vector cursor) {
		super(new Signature(Input.class));

		this.camera = camera;
		this.cursor = cursor;

		scene.setOnMouseMoved(e -> relCursor.set((float) e.getX(), (float) e.getY()));

		scene.setOnMousePressed((e) -> pressedKeys.add(e.getButton()));
		scene.setOnMouseReleased((e) -> pressedKeys.remove(e.getButton()));

		scene.setOnKeyPressed(e -> pressedKeys.add(e.getCode()));
		scene.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));
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
