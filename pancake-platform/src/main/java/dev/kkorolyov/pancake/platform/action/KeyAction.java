package dev.kkorolyov.pancake.platform.action;

import dev.kkorolyov.pancake.platform.action.MultiStageAction.ArmingOption;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.Signature;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Wraps a {@link MultiStageAction} and uses collections of {@link KeyCode} objects to arm.
 */
public class KeyAction extends Action {
	private final MultiStageAction delegate;
	private final Set<Enum> inputs = new HashSet<>();

	/** @see #KeyAction(MultiStageAction, Iterable) */
	public KeyAction(MultiStageAction delegate, Enum... inputs) {
		this(delegate, Arrays.asList(inputs));
	}
	/**
	 * Constructs a new key action.
	 * @param delegate wrapped multi-stage action
	 * @param inputs keys and buttons tied to action, when all pressed, counts as an activation, else counts as a deactivation
	 * @throws IllegalArgumentException if any element of {@code inputs} is not a {@link KeyCode} or {@link MouseButton}
	 */
	public KeyAction(MultiStageAction delegate, Iterable<Enum> inputs) {
		super(new Signature());

		this.delegate = delegate;
		for (Enum input : inputs) {
			if (!(input instanceof KeyCode || input instanceof MouseButton)) {
				throw new IllegalArgumentException("Not a valid input: " + input);
			}
			this.inputs.add(input);
		}
	}

	/**
	 * Arms the wrapped action after translating the intersection of expected inputs and given inputs into a {@link MultiStageAction.ArmingOption}.
	 * @param inputs current inputs
	 * @param dt seconds elapsed since the last invocation of this method
	 * @return {@code this}
	 */
	public KeyAction arm(Set<Enum> inputs, float dt) {
		delegate.arm(inputs.containsAll(this.inputs)
						? ArmingOption.ACTIVATE
						: ArmingOption.DEACTIVATE,
				dt);

		return this;
	}

	@Override
	protected void apply(Entity entity) {
		delegate.apply(entity);
	}
}
