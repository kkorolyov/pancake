package dev.kkorolyov.pancake.input;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import dev.kkorolyov.pancake.Entity;
import dev.kkorolyov.simplelogs.Level;
import dev.kkorolyov.simplelogs.Logger;
import dev.kkorolyov.simplelogs.format.Formatters;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

/**
 * Returns a different action in accordance with the current state of inputs.
 */
public class KeyAction {
	private static final float HOLD_THRESHOLD = .5f;
	private static final Logger log = Logger.getLogger(Level.DEBUG, Formatters.simple());

	private final Set<Enum<?>> keys = new LinkedHashSet<>();
	private final Consumer<? super Entity> onPress, onHold, onRelease;
	private float holdTime = 0;
	private KeyActionState state = KeyActionState.OPEN;

	/**
	 * Constructs a new {@code KeyAction} tied to a set of keys and buttons.
	 * @param onPress action returned on button press
	 * @param onHold action returned on button hold
	 * @param onRelease action returned on button release
	 * @param inputs keys and buttons tied to actions
	 * @throws IllegalArgumentException if any element of {@code inputs} is not a {@link KeyCode} or {@link MouseButton}
	 */
	public KeyAction(Consumer<? super Entity> onPress, Consumer<? super Entity> onHold, Consumer<? super Entity> onRelease, Enum<?>... inputs) {
		this.onPress = onPress;
		this.onHold = onHold;
		this.onRelease = onRelease;

		for (Enum<?> input : inputs) {
			if (!(input instanceof KeyCode || input instanceof MouseButton)) {
				throw new IllegalArgumentException("Not a valid key: " + input);
			}
			keys.add(input);
		}
	}

	/**
	 * Polls this {@code KeyAction} with all current active inputs.
	 * @param inputs current active inputs
	 * @param dt elapsed time in seconds since last poll
	 * @return appropriate action, or {@code null} if no appropriate action in the current state
	 */
	public Consumer<? super Entity> activate(Set<Enum<?>> inputs, float dt) {
		return state.activate(this, inputs.containsAll(keys), dt);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		KeyAction keyAction = (KeyAction) o;

		return Float.compare(keyAction.holdTime, holdTime) == 0 &&
					 Objects.equals(keys, keyAction.keys) &&
					 Objects.equals(onPress, keyAction.onPress) &&
					 Objects.equals(onHold, keyAction.onHold) &&
					 Objects.equals(onRelease, keyAction.onRelease) &&
					 Objects.equals(state, keyAction.state);
	}
	@Override
	public int hashCode() {
		return Objects.hash(keys, onPress, onHold, onRelease, holdTime, state);
	}

	private static abstract class KeyActionState {
		static final KeyActionState OPEN = new KeyActionState() {
			@Override
			Consumer<? super Entity> activate(KeyAction client, boolean allPressed, float dt) {
				if (allPressed) {
					log.debug("Keys pressed: {}", client.keys);

					client.holdTime = 0;
					client.state = PRESSED;
					return client.onPress;
				} else
					return null;
			}
		};
		static final KeyActionState PRESSED = new KeyActionState() {
			@Override
			Consumer<? super Entity> activate(KeyAction client, boolean allPressed, float dt) {
				if (allPressed) {
					client.holdTime += dt;

					if (client.holdTime >= HOLD_THRESHOLD) {
						log.debug("Keys held: {}", client.keys);

						client.state = WAITING;
						return client.onHold;
					} else
						return null;
				} else {
					log.debug("Keys released: {}", client.keys);

					client.state = OPEN;
					return client.onRelease;
				}
			}
		};
		static final KeyActionState WAITING = new KeyActionState() {
			@Override
			Consumer<? super Entity> activate(KeyAction client, boolean allPressed, float dt) {
				if (!allPressed) {
					log.debug("Keys released: {}", client.keys);

					client.state = OPEN;
					return client.onRelease;
				} else
					return null;
			}
		};

		abstract Consumer<? super Entity> activate(KeyAction client, boolean allPressed, float dt);
	}
}
