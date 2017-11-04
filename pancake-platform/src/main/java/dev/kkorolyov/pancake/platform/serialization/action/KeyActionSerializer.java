package dev.kkorolyov.pancake.platform.serialization.action;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.action.ActionRegistry;
import dev.kkorolyov.pancake.platform.action.KeyAction;
import dev.kkorolyov.pancake.platform.action.MultiStageAction;
import dev.kkorolyov.pancake.platform.serialization.AutoSerializer;
import dev.kkorolyov.pancake.platform.serialization.Serializer;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Serializes key actions.
 */
public class KeyActionSerializer extends ActionSerializer<KeyAction> {
	private final Serializer<Action, String> autoSerializer;

	/**
	 * Constructs a new key action serializer.
	 * @param context associated action registry
	 */
	public KeyActionSerializer(ActionRegistry context) {
		super("\\[[_a-zA-Z]+(,\\s*[_a-zA-Z]+)*]\\s*=\\s*.*", context);
		autoSerializer = new AutoSerializer<>(ActionSerializer.class, context);
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
		Action action = autoSerializer.read(s);

		return action instanceof MultiStageAction
				? (MultiStageAction) action
				: new MultiStageAction(action, null, null, 0);	// Hold threshold irrelevant
	}

	@Override
	public String write(KeyAction in) {
		return in.getInputs() + "=" + autoSerializer.write(in.getDelegate());
	}
}
