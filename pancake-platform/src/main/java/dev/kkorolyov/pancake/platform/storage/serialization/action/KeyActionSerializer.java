package dev.kkorolyov.pancake.platform.storage.serialization.action;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.action.ActionRegistry;
import dev.kkorolyov.pancake.platform.action.KeyAction;
import dev.kkorolyov.pancake.platform.action.MultiStageAction;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Serializes key actions.
 */
public class KeyActionSerializer extends ActionSerializer<KeyAction> {
	private static final ActionSerializer<Action> actionSerializer = new RegisteredActionSerializer();

	/**
	 * Constructs a new key action serializer.
	 */
	public KeyActionSerializer() {
		super("\\[[_a-zA-Z]+(,\\s*[_a-zA-Z]+)*]\\s*=\\s*" + actionSerializer.pattern());
	}

	@Override
	public KeyAction read(String out, ActionRegistry context) {
		String[] split = out.split("\\s*=\\s*", 2);
		String keysS = split[0], actionS = split[1];

		return new KeyAction(readAction(actionS, context), readKeys(keysS));
	}

	private Iterable<Enum> readKeys(String s) {
		return Arrays.stream(s.substring(1, s.length() - 1).split(",\\s*"))
				.map(this::toKey)
				.collect(Collectors.toList());

	}
	private Enum toKey(String s) {
		try {
			return KeyCode.valueOf(s);
		} catch (IllegalArgumentException e) {
			return MouseButton.valueOf(s);
		}
	}

	private MultiStageAction readAction(String s, ActionRegistry context) {
		Action action = actionSerializer.read(s, context);

		return action instanceof MultiStageAction
				? (MultiStageAction) action
				: new MultiStageAction(action, null, null, 0);	// Hold threshold irrelevant
	}

	@Override
	public String write(KeyAction in, ActionRegistry context) {
		// TODO
		return null;
	}
}
