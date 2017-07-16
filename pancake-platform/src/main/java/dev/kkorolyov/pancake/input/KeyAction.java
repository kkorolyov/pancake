package dev.kkorolyov.pancake.input;

import dev.kkorolyov.pancake.entity.Entity;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Signals an {@link Action} according to the current input state.
 */
public class KeyAction {
	private final Set<Enum<?>> keys = new LinkedHashSet<>();
	private final Action action;

	/**
	 * Constructs a new {@code KeyAction} tied to a set of keys and buttons.
	 * @param action signalled action
	 * @param inputs keys and buttons tied to action
	 * @throws IllegalArgumentException if any element of {@code inputs} is not a {@link KeyCode} or {@link MouseButton}
	 */
	public KeyAction(Action action, Enum<?>... inputs) {
		this(action, Arrays.asList(inputs));
	}
	/**
	 * Constructs a new {@code KeyAction} tied to a set of keys and buttons.
	 * @param action signalled action
	 * @param inputs keys and buttons tied to action
	 * @throws IllegalArgumentException if any element of {@code inputs} is not a {@link KeyCode} or {@link MouseButton}
	 */
	public KeyAction(Action action, Iterable<Enum<?>> inputs) {
		this.action = action;

		setKeys(inputs);
	}

	/**
	 * Signals this {@code KeyAction} with current active inputs.
	 * @param inputs current active inputs
	 * @param dt elapsed time in seconds since last poll
	 * @return appropriate action, or {@code null} if no appropriate action in the current state
	 */
	public Consumer<Entity> signal(Set<Enum<?>> inputs, float dt) {
		return action.signal(inputs.containsAll(keys), dt);
	}

	/**
	 * @param inputs keys and buttons tied to action
	 * @throws IllegalArgumentException if any element of {@code inputs} is not a {@link KeyCode} or {@link MouseButton}
	 */
	public void setKeys(Enum<?>... inputs) {
		setKeys(Arrays.asList(inputs));
	}
	/**
	 * @param inputs keys and buttons tied to action
	 * @throws IllegalArgumentException if any element of {@code inputs} is not a {@link KeyCode} or {@link MouseButton}
	 */
	public void setKeys(Iterable<Enum<?>> inputs) {
		keys.clear();

		for (Enum<?> input : inputs) {
			if (!(input instanceof KeyCode || input instanceof MouseButton)) {
				throw new IllegalArgumentException("Not a valid key: " + input);
			}
			keys.add(input);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		KeyAction keyAction = (KeyAction) o;

		return Objects.equals(keys, keyAction.keys) &&
					 Objects.equals(action, keyAction.action);
	}
	@Override
	public int hashCode() {
		return Objects.hash(keys, action);
	}
}
