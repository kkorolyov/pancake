package dev.kkorolyov.pancake.platform.action;

import dev.kkorolyov.pancake.platform.Config;
import dev.kkorolyov.pancake.platform.Resources;
import dev.kkorolyov.pancake.platform.storage.serialization.StringSerializer;
import dev.kkorolyov.pancake.platform.storage.serialization.string.action.ActionContainerStringSerializer;
import dev.kkorolyov.pancake.platform.storage.serialization.string.action.CollectiveActionStringSerializer;
import dev.kkorolyov.pancake.platform.storage.serialization.string.action.KeyActionStringSerializer;
import dev.kkorolyov.pancake.platform.storage.serialization.string.action.MultiStageActionStringSerializer;
import dev.kkorolyov.pancake.platform.storage.serialization.string.action.RegisteredActionStringSerializer;
import dev.kkorolyov.simplelogs.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

	private final List<StringSerializer<? extends Action>> actionSerializers = Arrays.asList(
			new RegisteredActionStringSerializer(this),
			new MultiStageActionStringSerializer(this),
			new CollectiveActionStringSerializer(this));
	private final ActionContainerStringSerializer actionContainerSerializer = new ActionContainerStringSerializer(this);
	private final KeyActionStringSerializer keyActionSerializer = new KeyActionStringSerializer(this);

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
	 */
	public void put(String name, Action action) {
		actions.put(name, action);
	}

	/**
	 * Parses a configuration file mapping new actions to combinations of known actions.
	 * @param path path to action configuration file
	 */
	public void put(String path) {
		Arrays.stream(Resources.string(path).split("\\R"))
				.filter(actionContainerSerializer::accepts)
				.peek(actionS -> log.info("Parsing action: {}", actionS))
				.map(actionContainerSerializer::read)
				.peek(actionContainer -> log.debug("Parsed to {}={}", actionContainer.name, actionContainer.action))
				.forEach(container -> put(container.name, container.action));
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
				.map(keyActionSerializer::read)
				.peek(keyAction -> log.debug("Parsed to {}", keyAction))
				.collect(Collectors.toList());
	}

	/**
	 * Reads an action using the most appropriate serializer.
	 * @param actionS string representation of action
	 * @return action instance
	 */
	public Action readAction(String actionS) {
		return actionSerializers.stream()
				.filter(serializer -> serializer.accepts(actionS))
				.peek(serializer -> log.debug("Parsing {} using {}", actionS, serializer))
				.findFirst()
				.map(serializer -> serializer.read(actionS))
				.orElseThrow(() -> new UnsupportedOperationException("No serializer for action: " + actionS));
	}
}
