package dev.kkorolyov.pancake.platform.action;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A {@link MultiStageAction} which uses collections of {@link KeyCode} objects to arm.
 */
public class KeyAction extends MultiStageAction {
	private final Set<Enum> inputs = new HashSet<>();

	/**
	 * Constructs a new key action.
	 * @see #KeyAction(Action, Action, Action, float, Iterable)
	 */
	public KeyAction(Action start, Action hold, Action end, float holdThreshold, Enum... inputs) {
		this(start, hold, end, holdThreshold, Arrays.asList(inputs));
	}
	/**
	 * Constructs a new key action.
	 * @param inputs keys and buttons tied to action, when all pressed, counts as an activation, else counts as a deactivation
	 * @throws IllegalArgumentException if any element of {@code inputs} is not a {@link KeyCode} or {@link MouseButton}
	 * @see MultiStageAction
	 */
	public KeyAction(Action start, Action hold, Action end, float holdThreshold, Iterable<Enum> inputs) {
		super(start, hold, end, holdThreshold);

		for (Enum input : inputs) {
			if (!(input instanceof KeyCode || input instanceof MouseButton)) {
				throw new IllegalArgumentException("Not a valid input: " + input);
			}
			this.inputs.add(input);
		}
	}

	/**
	 * Arms this action depending on whether the given inputs contain all inputs listened to by this action.
	 * @param inputs all current inputs
	 * @param dt seconds elapsed since the last invocation of this method
	 * @return {@code this}
	 */
	public MultiStageAction arm(Set<Enum> inputs, float dt) {
		return arm(inputs.containsAll(this.inputs)
				? ArmingOption.ACTIVATE
				: ArmingOption.DEACTIVATE,
				dt);
	}
}
