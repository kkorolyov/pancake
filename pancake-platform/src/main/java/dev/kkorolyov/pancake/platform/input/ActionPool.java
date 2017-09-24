package dev.kkorolyov.pancake.platform.input;

import dev.kkorolyov.pancake.platform.Config;
import dev.kkorolyov.simplelogs.Logger;
import dev.kkorolyov.simpleprops.Properties;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Maintains a collection of known actions applicable on entities.
 */
public class ActionPool {
	private static final Logger log = Config.getLogger(ActionPool.class);

	private final Map<String, Action> actions = new HashMap<>();

	/**
	 * Retrieves an action by name.
	 * @param name action identifier
	 * @return action mapped to {@code name}, or the {@code NOOP} action if no such action
	 */
	public Action get(String name) {
		if (name == null) return Action.NOOP;

		Action action = actions.get(name);
		return action == null ? Action.NOOP : action;
	}

	/**
	 * Parses a configuration file mapping new actions to combinations of known actions.
	 * A single configuration entry takes the form {@code ACTION=START_ACTION, HOLD_ACTION, STOP_ACTION}.
	 * Unknown or empty action names resolve to the {@code NOOP} action.
	 * @param actionConfig action configuration file
	 */
	public void put(Properties actionConfig) {
		for (Entry<String, String> entry : actionConfig) {
			put(parseAction(entry.getKey(), split(entry.getValue())));
			log.info("Parsed action config entry: {}", entry);
		}
	}
	/**
	 * Adds an action defined by existing known actions to the pool.
	 * @param name action name
	 * @param start start action identifier
	 * @param hold hold action identifier
	 * @param stop stop action identifier
	 */
	public void put(String name, String start, String hold, String stop) {
		put(new Action(name, get(start), get(hold), get(stop)));
	}
	/**
	 * Adds an action to the pool.
	 * If an action of the same name already exists in the pool, it is replaced with the new action.
	 * @param action added action
	 */
	public void put(Action action) {
		actions.put(action.getName(), action);
	}

	/**
	 * Parses a configuration file defining keys bound to actions maintained by this action pool.
	 * A single configuration entry takes the form {@code [KEY1, KEY2, ...]=ACTION}, where {@code ACTION} is either a single action or a set of 3 actions independently defining start, hold, and stop events.
	 * Unknown or empty action names resolve to the {@code NOOP} action.
	 * @param keyConfig key configuration file
	 * @return collection of {@link KeyAction} objects defined in {@code config}
	 */
	public Iterable<KeyAction> parseConfig(Properties keyConfig) {
		List<KeyAction> keyActions = new ArrayList<>();

		for (Entry<String, String> entry : keyConfig) {
			String[] keyNames = split(entry.getKey());
			String[] actionNames = split(entry.getValue());

			keyActions.add(new KeyAction(parseAction(Arrays.toString(actionNames), actionNames),
																	 parseKeys(keyNames)));

			log.info("Parsed key config entry: {}", entry);
		}
		return keyActions;
	}
	private Enum<?>[] parseKeys(String[] keyNames) {
		Enum<?>[] keys = new Enum<?>[keyNames.length];

		for (int i = 0; i < keys.length; i++) {
			keys[i] = parseKey(keyNames[i]);
		}
		return keys;
	}
	private Enum<?> parseKey(String keyName) {
		try {
			return KeyCode.valueOf(keyName);
		} catch (IllegalArgumentException e) {
			try {
				return MouseButton.valueOf(keyName);
			} catch (IllegalArgumentException e1) {
				throw new IllegalArgumentException("No such key or mouse button: " + keyName);
			}
		}
	}

	private Action parseAction(String name, String[] actionNames) {
		switch (actionNames.length) {
			case 1:
				return get(actionNames[0]);
			case 2:
				return new Action(name, get(actionNames[0]), get(actionNames[1]), Action.NOOP);
			default:
				return new Action(name, get(actionNames[0]), get(actionNames[1]), get(actionNames[2]));
		}
	}

	private String[] split(String array) {
		String trim = "^\\[|]$";
		String delimiter = ",\\s*";

		return array.replaceAll(trim, "").split(delimiter);
	}
}
