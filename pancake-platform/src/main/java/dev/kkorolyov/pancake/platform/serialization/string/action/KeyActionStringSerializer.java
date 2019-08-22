package dev.kkorolyov.pancake.platform.serialization.string.action;

import dev.kkorolyov.pancake.platform.Registry;
import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.action.KeyAction;
import dev.kkorolyov.pancake.platform.action.MultiStageAction;
import dev.kkorolyov.pancake.platform.serialization.AutoSerializer;
import dev.kkorolyov.pancake.platform.serialization.Serializer;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Serializes key actions.
 */
public final class KeyActionStringSerializer extends ActionStringSerializer<KeyAction> {
	private static final Pattern SPLIT = Pattern.compile("\\s*=\\s*");
	private static final Pattern SPLIT_KEYS = Pattern.compile(",\\s*");

	private final Serializer<Action, String> autoSerializer;

	/**
	 * Constructs a new key action serializer.
	 * @param context associated action registry
	 */
	public KeyActionStringSerializer(Registry<String, Action> context) {
		super("\\[[_a-zA-Z]+(,\\s*[_a-zA-Z]+)*]\\s*=\\s*.*", context);
		autoSerializer = new AutoSerializer<>(ActionStringSerializer.class, context);
	}

	@Override
	public KeyAction read(String out) {
		String[] split = SPLIT.split(out, 2);
		String keysS = split[0], actionS = split[1];

		return new KeyAction(readAction(actionS), readKeys(keysS));
	}

	private Iterable<Enum> readKeys(String s) {
		return Arrays.stream(SPLIT_KEYS.split(s.substring(1, s.length() - 1)))
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
