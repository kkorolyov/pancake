package dev.kkorolyov.pancake.platform.serialization.action;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.action.ActionRegistry;
import dev.kkorolyov.pancake.platform.action.CollectiveAction;
import dev.kkorolyov.pancake.platform.serialization.AutoContextualSerializer;
import dev.kkorolyov.pancake.platform.serialization.ContextualSerializer;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Serializes collective actions.
 */
public class CollectiveActionSerializer extends ActionSerializer<CollectiveAction> {
	private static final ContextualSerializer<Action, String, ActionRegistry> autoSerializer = new AutoContextualSerializer(ActionSerializer.class);

	/**
	 * Constructs a new collective action serializer
	 */
	public CollectiveActionSerializer() {
		super("\\{.+(,\\s?.+)+}");
	}

	@Override
	public CollectiveAction read(String out, ActionRegistry context) {
		return new CollectiveAction(Arrays.stream(out.substring(1, out.length() - 1).split(",\\s?"))
				.map(s -> autoSerializer.read(s, context))
				.collect(Collectors.toList()));
	}
	@Override
	public String write(CollectiveAction in, ActionRegistry context) {
		return in.getDelegates().stream()
				.map(delegate -> autoSerializer.write(delegate, context))
				.collect(Collectors.joining(", ", "{", "}"));
	}
}
