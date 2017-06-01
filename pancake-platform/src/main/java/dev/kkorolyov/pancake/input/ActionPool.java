package dev.kkorolyov.pancake.input;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Consumer;

import dev.kkorolyov.pancake.Entity;
import dev.kkorolyov.simplelogs.Level;
import dev.kkorolyov.simplelogs.Logger;
import dev.kkorolyov.simplelogs.format.Formatters;
import dev.kkorolyov.simpleprops.Properties;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

/**
 * Maintains a collection of known actions applicable on entities.
 */
public class ActionPool {
	private static final int PRESS = 0;
	private static final int HOLD = 1;
	private static final int RELEASE = 2;
	private static final Logger log = Logger.getLogger(Level.DEBUG, Formatters.simple());

	private final Map<String, Consumer<Entity>> actions = new HashMap<>();

	/**
	 * Retrieves an action by name.
	 * @param name action identifier, case-insensitive
	 * @return action mapped to {@code name}, or {@code null} if no such action
	 */
	public Consumer<Entity> get(String name) {
		return actions.get(name.toUpperCase());
	}
	/**
	 * Adds an action to the pool.
	 * If an action of the same name already exists in the pool, it is replaced with the new action.
	 * @param name identifier
	 * @param action action logic
	 */
	public void put(String name, Consumer<Entity> action) {
		actions.put(name.toUpperCase(), action);
	}

	/**
	 * Parses a configuration file defining keys bound to actions maintained by this action pool.
	 * A single configuration entry takes the form {@code [KEY1, KEY2, ...]=PRESS_ACTION, HOLD_ACTION, RELEASE_ACTION}.
	 * Unknown, empty, or omitted action names resolve to {@code null} actions.
	 * @param keyConfig parsed key configuration file
	 * @return collection of {@link KeyAction} objects defined in {@code config}
	 */
	public Iterable<KeyAction> parseConfig(Properties keyConfig) {
		List<KeyAction> keyActions = new ArrayList<>();

		for (Entry<String, String> entry : keyConfig) {
			String[] keyNames = split(entry.getKey());
			String[] actionNames = split(entry.getValue());

			keyActions.add(new KeyAction(parseAction(actionNames, PRESS),
																	 parseAction(actionNames, HOLD),
																	 parseAction(actionNames, RELEASE),
																	 parseKeys(keyNames)));

			log.info("Parsed key config: {}={}", Arrays.toString(keyNames), Arrays.toString(actionNames));
		}
		return keyActions;
	}
	private String[] split(String array) {
		String trim = "^\\[|]$";
		String delimiter = ",\\s*";

		return array.replaceAll(trim, "").split(delimiter);
	}

	private Enum<?>[] parseKeys(String[] keyNames) {
		Enum<?>[] keys = new Enum<?>[keyNames.length];

		for (int i = 0; i < keys.length; i++) {
			keys[i] = parseKey(keyNames[i]);
		}
		log.debug("Parsed keys: {} => {}", Arrays.toString(keyNames), Arrays.toString(keys));
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

	private Consumer<Entity> parseAction(String[] actionNames, int index) {
		if (actionNames.length > index) {
			Consumer<Entity> action = get(actionNames[index]);

			log.debug("Parsed action: {} => {}", actionNames[index], action);
			return action;
		} else {
			return null;
		}
	}
}
