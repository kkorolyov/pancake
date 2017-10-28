package dev.kkorolyov.pancake.platform.action;

import dev.kkorolyov.pancake.platform.Config;
import dev.kkorolyov.pancake.platform.Resources;
import dev.kkorolyov.pancake.platform.serialization.action.ActionContainerSerializer;
import dev.kkorolyov.pancake.platform.serialization.action.KeyActionSerializer;
import dev.kkorolyov.simplelogs.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * A collection of base actions.
 */
public class ActionRegistry {
	private static final Logger log = Config.getLogger(ActionRegistry.class);

	private final Map<String, Action> actions = new HashMap<>();

	private final ActionContainerSerializer actionContainerSerializer = new ActionContainerSerializer();
	private final KeyActionSerializer keyActionSerializer = new KeyActionSerializer();

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
	 * @return name bound to {@code action}
	 * @throws NoSuchElementException if this registry does not contain {@code action}
	 */
	public String getName(Action action) {
		return actions.entrySet().stream()
				.filter(entry -> entry.getValue().equals(action))
				.findFirst()
				.map(Entry::getKey)
				.orElseThrow(NoSuchElementException::new);
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
				.filter(actionContainerSerializer::accepts)
				.peek(actionS -> log.info("Parsing action: {}", actionS))
				.map(actionS -> actionContainerSerializer.read(actionS, this))
				.peek(actionContainer -> log.debug("Parsed to {}={}", actionContainer.name, actionContainer.action))
				.forEach(container -> put(container.name, container.action));
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
				.filter(keyActionSerializer::accepts)
				.peek(actionS -> log.info("Parsing key action: {}", actionS))
				.map(actionS -> keyActionSerializer.read(actionS, this))
				.peek(keyAction -> log.debug("Parsed to {}", keyAction))
				.collect(Collectors.toList());
	}
}
