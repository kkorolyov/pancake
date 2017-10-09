package dev.kkorolyov.pancake.platform.action;

import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.pancake.platform.entity.Signature;

/**
 * An {@link Action} which applies a different action depending on its current state.
 */
public class MultiStageAction extends Action {
	private final Action start, hold, end;
	private ArmingOption armingOption;
	private State state = State.INACTIVE;

	/**
	 * Constructs a new multi-stage action.
	 * @param start action applied when this action is "activated"
	 * @param hold action applied the first time this action is signalled after it has been "activated"
	 * @param end action applied when this action is "deactivated"
	 */
	public MultiStageAction(Action start, Action hold, Action end) {
		super(new Signature());

		this.start = start;
		this.hold = hold;
		this.end = end;
	}

	/**
	 * Arms this action to move to the next state in its state sequence, according to the given arming option.
	 * The next time {@link #apply(Entity)} is invoked on this action, its state will change according to the current arming option.
	 * @param armingOption option influencing the next state this action moves to after its next application
	 * @return {@code this}
	 */
	public MultiStageAction arm(ArmingOption armingOption) {
		this.armingOption = armingOption;
		return this;
	}

	/**
	 * Applies this action to an entity if it is currently armed.
	 * If this action is not armed, does nothing.
	 * Disarms after application.
	 */
	@Override
	protected void apply(Entity entity) {
		if (armingOption != null) state.apply(entity, this);

		armingOption = null;
	}

	/**
	 * Arms a {@link MultiStageAction} to change state in a particular way next time it is applied.
	 */
	public enum ArmingOption {
		ACTIVATE,
		DEACTIVATE
	}

	private static abstract class State {
		static final State INACTIVE = new State() {
			@Override
			void apply(Entity entity, MultiStageAction client) {
				switch (client.armingOption) {
					case ACTIVATE:
						client.start.accept(entity);
						client.state = ACTIVE;
						break;
				}
			}
		};
		static final State ACTIVE = new State() {
			@Override
			void apply(Entity entity, MultiStageAction client) {
				switch (client.armingOption) {
					case ACTIVATE:
						client.hold.accept(entity);
						client.state = DECAYED;
						break;
					case DEACTIVATE:
						client.end.accept(entity);
						client.state = INACTIVE;
						break;
				}
			}
		};
		static final State DECAYED = new State() {
			@Override
			void apply(Entity entity, MultiStageAction client) {
				switch (client.armingOption) {
					case DEACTIVATE:
						client.end.accept(entity);
						client.state = INACTIVE;
						break;
				}
			}
		};

		abstract void apply(Entity entity, MultiStageAction client);
	}
}
