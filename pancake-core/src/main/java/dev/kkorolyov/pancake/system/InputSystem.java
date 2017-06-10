package dev.kkorolyov.pancake.system;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import dev.kkorolyov.pancake.entity.Entity;
import dev.kkorolyov.pancake.GameSystem;
import dev.kkorolyov.pancake.entity.Signature;
import dev.kkorolyov.pancake.component.Input;
import dev.kkorolyov.pancake.input.KeyAction;
import javafx.scene.Scene;

/**
 * Applies actions using current player input.
 */
public class InputSystem extends GameSystem {
	private final Set<Enum<?>> pressedKeys = new HashSet<>();

	/**
	 * Constructs a new input system.
	 * @param scene scene providing player input
	 */
	public InputSystem(Scene scene) {
		super(new Signature(Input.class));

		scene.setOnKeyPressed(e -> pressedKeys.add(e.getCode()));
		scene.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));
		scene.setOnMousePressed((e) -> pressedKeys.add(e.getButton()));
		scene.setOnMouseReleased((e) -> pressedKeys.remove(e.getButton()));
	}

	@Override
	public void update(Entity entity, float dt) {
		for (KeyAction keyAction : entity.get(Input.class).getActions()) {
			Consumer<? super Entity> action = keyAction.signal(pressedKeys, dt);
			if (action != null) action.accept(entity);
		}
	}
}
