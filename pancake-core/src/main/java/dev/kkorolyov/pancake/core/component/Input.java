package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.action.KeyAction;
import dev.kkorolyov.pancake.platform.entity.Component;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * An entity controller which responds to keyboard and mouse input.
 */
public class Input implements Component {
	private final Set<KeyAction> keyActions = new LinkedHashSet<>();
	private boolean facesCursor;

	/**
	 * Constructs a new controller which receives input from key and mouse button events generated by a {@code Scene}.
	 * @param facesCursor whether this controller should request that its containing entity faces the mouse cursor
	 * @param keyActions actions to listen for
	 */
	public Input(boolean facesCursor, KeyAction... keyActions) {
		this(facesCursor, Arrays.asList(keyActions));
	}
	/**
	 * Constructs a new controller which receives input from key and mouse button events generated by a {@code Scene}.
	 * @param facesCursor whether this controller should request that its containing entity faces the mouse cursor
	 * @param keyActions actions to listen for
	 */
	public Input(boolean facesCursor, Iterable<KeyAction> keyActions) {
		setFacesCursor(facesCursor);
		setActions(keyActions);
	}

	/** @return registered key actions */
	public Iterable<KeyAction> getActions() {
		return keyActions;
	}

	/**
	 * Clears existing actions before applying new {@code keyActions}.
	 * @param keyActions actions to listen for
	 */
	public void setActions(KeyAction... keyActions) {
		setActions(Arrays.asList(keyActions));
	}
	/**
	 * Clears existing actions before applying new {@code keyActions}.
	 * @param keyActions actions to listen for
	 */
	public void setActions(Iterable<KeyAction> keyActions) {
		this.keyActions.clear();

		keyActions.forEach(this.keyActions::add);
	}

	/** @return whether this controller requests that its containing entity faces the mouse cursor */
	public boolean facesCursor() {
		return facesCursor;
	}
	/** @param facesCursor if {@code true}, this controller requests that its containing entity faces the mouse cursor */
	public void setFacesCursor(boolean facesCursor) {
		this.facesCursor = facesCursor;
	}
}
