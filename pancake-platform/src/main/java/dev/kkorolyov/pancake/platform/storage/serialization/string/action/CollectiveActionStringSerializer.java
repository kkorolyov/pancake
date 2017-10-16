package dev.kkorolyov.pancake.platform.storage.serialization.string.action;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.action.ActionRegistry;
import dev.kkorolyov.pancake.platform.action.CollectiveAction;
import dev.kkorolyov.pancake.platform.storage.serialization.StringSerializer;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Serializes collective actions.
 */
public class CollectiveActionStringSerializer extends StringSerializer<CollectiveAction> {
	private final ActionRegistry registry;

	/**
	 * Constructs a new collective action serializer
	 * @param registry associated action registry
	 */
	public CollectiveActionStringSerializer(ActionRegistry registry) {
		super("\\{.+(,\\s?.+)+}");
		this.registry = registry;
	}

	@Override
	public CollectiveAction read(String out) {
		Iterable<Action> delegates = Arrays.stream(out.substring(1, out.length() - 1).split(",\\s?"))
				.map(registry::readAction)
				.collect(Collectors.toList());

		return new CollectiveAction(delegates);
	}
	@Override
	public String write(CollectiveAction in) {
		// TODO
		return null;
	}
}
