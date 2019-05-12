package dev.kkorolyov.pancake.platform.serialization.string.action;

import dev.kkorolyov.pancake.platform.Config;
import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.action.ActionRegistry;
import dev.kkorolyov.pancake.platform.action.MultiStageAction;
import dev.kkorolyov.pancake.platform.serialization.AutoSerializer;
import dev.kkorolyov.pancake.platform.serialization.Serializer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Serializes multi-stage actions.
 */
public class MultiStageActionStringSerializer extends ActionStringSerializer<MultiStageAction> {
	private final Serializer<Action, String> autoSerializer;
	private final float holdThreshold = Float.parseFloat(Config.config.get("holdThreshold"));

	public MultiStageActionStringSerializer() {
		this(null);
	}
	/**
	 * Constructs a new multi-stage action serializer.
	 * @param context associated action registry
	 */
	public MultiStageActionStringSerializer(ActionRegistry context) {
		super("\\{.*(,.*){2}}", context);
		autoSerializer = new AutoSerializer<>(ActionStringSerializer.class, context);
	}

	@Override
	public MultiStageAction read(String out) {
		List<Action> actions = Arrays.stream(out.substring(1, out.length() - 1).split(",\\s?(?![^\\[]*])"))
				.map(actionS -> actionS.length() <= 0 ? null : autoSerializer.read(actionS))
				.collect(Collectors.toList());

		return new MultiStageAction(
				actions.get(0),
				actions.get(1),
				actions.get(2),
				holdThreshold);
	}
	@Override
	public String write(MultiStageAction in) {
		return Stream.of(in.getStart(), in.getHold(), in.getEnd())
				.map(action -> action == null ? "" : autoSerializer.write(action))
				.collect(Collectors.joining(",", "{", "}"));
	}
}
