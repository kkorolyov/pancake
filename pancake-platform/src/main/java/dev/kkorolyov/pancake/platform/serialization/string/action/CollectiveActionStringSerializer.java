package dev.kkorolyov.pancake.platform.serialization.string.action;

import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.action.ActionRegistry;
import dev.kkorolyov.pancake.platform.action.CollectiveAction;
import dev.kkorolyov.pancake.platform.serialization.AutoSerializer;
import dev.kkorolyov.pancake.platform.serialization.Serializer;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Serializes collective actions.
 */
public class CollectiveActionStringSerializer extends ActionStringSerializer<CollectiveAction> {
	private final Serializer<Action, String> autoSerializer;

	/**
	 * Constructs a new collective action serializer
	 * @param context associated action registry
	 */
	public CollectiveActionStringSerializer(ActionRegistry context) {
		super("\\[.+(,\\s?.+)+]", context);
		autoSerializer = new AutoSerializer<>(ActionStringSerializer.class, context);
	}

	@Override
	public CollectiveAction read(String out) {
		return new CollectiveAction(Arrays.stream(out.substring(1, out.length() - 1).split(",\\s?"))
				.map(autoSerializer::read)
				.collect(Collectors.toList()));
	}
	@Override
	public String write(CollectiveAction in) {
		return in.getDelegates().stream()
				.map(autoSerializer::write)
				.collect(Collectors.joining(", ", "[", "]"));
	}
}
