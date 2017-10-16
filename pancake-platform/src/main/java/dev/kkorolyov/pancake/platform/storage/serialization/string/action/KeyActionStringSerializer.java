package dev.kkorolyov.pancake.platform.storage.serialization.string.action;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.action.ActionRegistry;
import dev.kkorolyov.pancake.platform.action.KeyAction;
import dev.kkorolyov.pancake.platform.action.MultiStageAction;
import dev.kkorolyov.pancake.platform.storage.serialization.StringSerializer;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Serializes key actions.
 */
public class KeyActionStringSerializer extends StringSerializer<KeyAction> {
	private final ActionRegistry registry;

	/**
	 * Constructs a new key action serializer.
	 * @param registry associated action registry
	 */
	public KeyActionStringSerializer(ActionRegistry registry) {
		super("\\[[_a-zA-Z]+(,\\s*[_a-zA-Z]+)*]\\s*=\\s*.+");
		this.registry = registry;
	}

	@Override
	public KeyAction read(String out) {
		String[] split = out.split("\\s*=\\s*", 2);
		String keysS = split[0], actionS = split[1];

		return new KeyAction(readAction(actionS), readKeys(keysS));
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

	private MultiStageAction readAction(String s) {
		Action action = registry.readAction(s);

		return action instanceof MultiStageAction
				? (MultiStageAction) action
				: new MultiStageAction(action, null, null, 0);	// Hold threshold irrelevant
	}

	@Override
	public String write(KeyAction in) {
		// TODO
		return null;
	}
}
