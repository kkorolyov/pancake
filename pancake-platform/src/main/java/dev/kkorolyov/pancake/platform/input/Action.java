package dev.kkorolyov.pancake.platform.input;

import dev.kkorolyov.pancake.platform.Config;
import dev.kkorolyov.pancake.platform.entity.Entity;
import dev.kkorolyov.simplelogs.Logger;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A collection of 3 distinct events (start, hold, and stop) organized as a finite state machine.
 * When queried, returns an event depending on the current state.
 * The current state depends on the duration for which this action is activated or deactivated.
 */
public class Action {
	/** The action which contains no events */
	public static final Action NOOP = new Action("NOOP", (Consumer<Entity>) null, null, null);

	private static final float HOLD_THRESHOLD = .5f;
	private static final Logger log = Config.getLogger(Action.class);

	private final String name;
	private final Consumer<Entity> start, hold, stop;
	private float holdTime = 0;
	private ActionState state = ActionState.INACTIVE;

	/**
	 * Constructs a new action from other actions.
	 * The {@code start} event of each parameter action populates the corresponding event of this action.
	 * @param name action name
	 * @param start action returned on initial activation
	 * @param hold action returned after being active for some "hold threshold"
	 * @param stop action returned on deactivation
	 */
	public Action(String name, Action start, Action hold, Action stop) {
		this(name, start.start, hold.start, stop.start);
	}
	/**
	 * Constructs a new action which contains only a {@code start} event.
	 * @param name action name
	 * @param start event returned on initial activation
	 */
	public Action(String name, Consumer<Entity> start) {
		this(name, start, null, null);
	}
	/**
	 * Constructs a new action.
	 * @param name action name
	 * @param start event returned on initial activation
	 * @param hold event returned after being active for some "hold threshold"
	 * @param stop event returned on deactivation
	 */
	public Action(String name, Consumer<Entity> start, Consumer<Entity> hold, Consumer<Entity> stop) {
		this.name = name;
		this.start = start;
		this.hold = hold;
		this.stop = stop;
	}

	/**
	 * Signals an activation or deactivation to this action.
	 * @param active {@code true} for activation, {@code false} for deactivation
	 * @param dt elapsed time in seconds since previous signal
	 * @return event corresponding to current state, or {@code null} if no such event
	 */
	public Consumer<Entity> signal(boolean active, float dt) {
		return state.signal(this, active, dt);
	}

	/** @return action name */
	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Action action = (Action) o;

		return Float.compare(action.holdTime, holdTime) == 0 &&
					 Objects.equals(name, action.name) &&
					 Objects.equals(start, action.start) &&
					 Objects.equals(hold, action.hold) &&
					 Objects.equals(stop, action.stop) &&
					 Objects.equals(state, action.state);
	}
	@Override
	public int hashCode() {
		return Objects.hash(name, start, hold, stop, holdTime, state);
	}

	@Override
	public String toString() {
		return "Action{" + name + "}";
	}

	private static abstract class ActionState {
		static final ActionState INACTIVE = new ActionState() {
			@Override
			Consumer<Entity> signal(Action client, boolean active, float dt) {
				if (active) {
					log.debug("Action activated: {}", client);

					client.holdTime = 0;
					client.state = ACTIVE;
					return client.start;
				} else
					return null;
			}
		};
		static final ActionState ACTIVE = new ActionState() {
			@Override
			Consumer<Entity> signal(Action client, boolean active, float dt) {
				if (active) {
					client.holdTime += dt;

					if (client.holdTime >= HOLD_THRESHOLD) {
						log.debug("Action held: {}", client);

						client.state = DECAYED;
						return client.hold;
					} else
						return null;
				} else {
					log.debug("Action deactivated: {}", client);

					client.state = INACTIVE;
					return client.stop;
				}
			}
		};
		static final ActionState DECAYED = new ActionState() {
			@Override
			Consumer<Entity> signal(Action client, boolean active, float dt) {
				if (!active) {
					log.debug("Action deactivated: {}", client);

					client.state = INACTIVE;
					return client.stop;
				} else
					return null;
			}
		};

		abstract Consumer<Entity> signal(Action client, boolean active, float dt);
	}
}
