package dev.kkorolyov.pancake.platform.action;

import dev.kkorolyov.pancake.platform.action.MultiStageAction.ArmingOption;
import dev.kkorolyov.pancake.platform.entity.Entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Wraps a {@link MultiStageAction} and uses collections of input enums to arm.
 */
public class KeyAction implements Action {
	private final MultiStageAction delegate;
	private final Set<Enum<?>> inputs = new HashSet<>();

	/** @see #KeyAction(MultiStageAction, Iterable) */
	public KeyAction(MultiStageAction delegate, Enum<?>... inputs) {
		this(delegate, Arrays.asList(inputs));
	}
	/**
	 * Constructs a new key action.
	 * @param delegate wrapped multi-stage action
	 * @param inputs keys and buttons tied to {@code delegate}; when all pressed, counts as an activation, else counts as a deactivation
	 */
	public KeyAction(MultiStageAction delegate, Iterable<Enum<?>> inputs) {
		this.delegate = delegate;
		inputs.forEach(this.inputs::add);
	}

	/**
	 * Arms the wrapped action after translating the intersection of expected inputs and given inputs into a {@link MultiStageAction.ArmingOption}.
	 * @param inputs current inputs
	 * @param dt {@code ns} elapsed since the last invocation of this method
	 * @return {@code this}
	 */
	public KeyAction arm(Collection<Enum<?>> inputs, long dt) {
		delegate.arm(
				inputs.containsAll(this.inputs)
						? ArmingOption.ACTIVATE
						: ArmingOption.DEACTIVATE,
				dt
		);
		return this;
	}

	@Override
	public void apply(Entity entity) {
		delegate.apply(entity);
	}

	/** @return wrapped multi-stage action */
	public MultiStageAction getDelegate() {
		return delegate;
	}
	/** @return keys and buttons tied to delegate action */
	public Set<Enum<?>> getInputs() {
		return inputs;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;

		KeyAction o = (KeyAction) obj;
		return Objects.equals(delegate, o.delegate)
				&& Objects.equals(inputs, o.inputs);
	}
	@Override
	public int hashCode() {
		return Objects.hash(delegate, inputs);
	}
}
