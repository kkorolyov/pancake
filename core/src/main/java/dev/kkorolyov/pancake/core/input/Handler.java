package dev.kkorolyov.pancake.core.input;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.action.MultiStageAction;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toUnmodifiableSet;

/**
 * Wraps a {@link MultiStageAction} and uses collections of input enums to arm it.
 */
public class Handler {
	private final MultiStageAction action;
	private final Set<Enum<?>> inputs;

	/** @see #Handler(MultiStageAction, Iterable) */
	public Handler(MultiStageAction action, Enum<?>... inputs) {
		this(action, Arrays.asList(inputs));
	}
	/**
	 * Constructs a new key action.
	 * @param action wrapped multi-stage action
	 * @param inputs keys and buttons tied to {@code delegate}; when all pressed, counts as an activation, else counts as a deactivation
	 */
	public Handler(MultiStageAction action, Iterable<Enum<?>> inputs) {
		this.action = action;
		this.inputs = StreamSupport.stream(inputs.spliterator(), false)
				.collect(toUnmodifiableSet());
	}

	/**
	 * Arms the wrapped action after translating the intersection of expected inputs and given inputs into a {@link MultiStageAction.ArmingOption}.
	 * @param inputs current inputs
	 * @param dt {@code ns} elapsed since the last invocation of this method
	 * @return armed wrapped action
	 */
	public Action arm(Collection<Enum<?>> inputs, long dt) {
		return action.arm(
				inputs.containsAll(this.inputs)
						? MultiStageAction.ArmingOption.ACTIVATE
						: MultiStageAction.ArmingOption.DEACTIVATE,
				dt
		);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;

		Handler o = (Handler) obj;
		return Objects.equals(action, o.action)
				&& Objects.equals(inputs, o.inputs);
	}
	@Override
	public int hashCode() {
		return Objects.hash(action, inputs);
	}
}
