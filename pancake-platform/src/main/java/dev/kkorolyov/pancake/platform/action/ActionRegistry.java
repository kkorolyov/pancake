package dev.kkorolyov.pancake.platform.action;

import dev.kkorolyov.pancake.platform.Config;
import dev.kkorolyov.pancake.platform.Resources;
import dev.kkorolyov.pancake.platform.serialization.string.action.ActionAssignmentStringSerializer;
import dev.kkorolyov.pancake.platform.serialization.string.action.KeyActionStringSerializer;
import dev.kkorolyov.simplelogs.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * A collection of base actions.
 */
public class ActionRegistry {
	private static final Logger log = Config.getLogger(ActionRegistry.class);

	private final Map<String, Action> actions = new HashMap<>();

	private final ActionAssignmentStringSerializer actionAssignmentStringSerializer = new ActionAssignmentStringSerializer(this);
	private final KeyActionStringSerializer keyActionStringSerializer = new KeyActionStringSerializer(this);

	/**
	 * Retrieves an action by name.
	 * @param name action identifier
	 * @return action mapped to {@code name}, or {@code null} if no such action
	 */
	public Action get(String name) {
		return actions.get(name);
	}
	/**
	 * Returns the name under which an action is stored in this registry.
	 * @param action action to get associated name for
	 * @return name bound to {@code action}, or {@code null} if no such action
	 */
	public String getName(Action action) {
		return actions.entrySet().stream()
				.filter(entry -> entry.getValue().equals(action))
				.findFirst()
				.map(Entry::getKey)
				.orElse(null);
	}

	/**
	 * Adds an action.
	 * If an action of the same name already exists, it is replaced with the new action.
	 * @param name action identifier
	 * @param action action to add
	 * @return {@code this}
	 */
	public ActionRegistry put(String name, Action action) {
		actions.put(name, action);
		return this;
	}

	/**
	 * Parses a configuration file mapping new actions to combinations of known actions.
	 * @param path path to action configuration file
	 * @return {@code this}
	 */
	public ActionRegistry put(String path) {
		Arrays.stream(Resources.string(path).split("\\R"))
				.filter(actionAssignmentStringSerializer::accepts)
				.peek(actionS -> log.info("Parsing action: {}", actionS))
				.forEach(actionAssignmentStringSerializer::read);

		return this;
	}

	/**
	 * Parses a configuration file binding keys to actions maintained by this action pool.
	 * A single configuration entry takes the form {@code [KEY1, KEY2, ...]=ACTION}.
	 * @param path path to key configuration file
	 * @return parsed key actions
	 */
	public Iterable<KeyAction> readKeys(String path) {
		return Arrays.stream(Resources.string(path).split("\\R"))
				.filter(keyActionStringSerializer::accepts)
				.peek(actionS -> log.info("Parsing key action: {}", actionS))
				.map(keyActionStringSerializer::read)
				.peek(keyAction -> log.debug("Parsed to {}", keyAction))
				.collect(Collectors.toList());
	}
}
