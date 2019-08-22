package dev.kkorolyov.pancake.platform.serialization.string.action;

import dev.kkorolyov.pancake.platform.Registry;
import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.action.CollectiveAction;
import dev.kkorolyov.pancake.platform.serialization.AutoSerializer;
import dev.kkorolyov.pancake.platform.serialization.Serializer;

import java.util.Arrays;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * Serializes collective actions.
 */
public final class CollectiveActionStringSerializer extends ActionStringSerializer<CollectiveAction> {
	private static final Pattern SPLIT = Pattern.compile(",\\s?");

	private final Serializer<Action, String> autoSerializer;

	// Module service provider
	public CollectiveActionStringSerializer() {
		this(null);
	}
	/**
	 * Constructs a new collective action serializer
	 * @param context associated action registry
	 */
	public CollectiveActionStringSerializer(Registry<String, Action> context) {
		super("\\[.+(,\\s?.+)+]", context);
		autoSerializer = new AutoSerializer<>(ActionStringSerializer.class, context);
	}

	@Override
	public CollectiveAction read(String out) {
		return new CollectiveAction(
				Arrays.stream(SPLIT.split(out.substring(1, out.length() - 1)))
						.map(autoSerializer::read)
						.collect(toList())
		);
	}
	@Override
	public String write(CollectiveAction in) {
		return in.getDelegates().stream()
				.map(autoSerializer::write)
				.collect(joining(", ", "[", "]"));
	}
}
