package dev.kkorolyov.pancake.platform.serialization.action;

import dev.kkorolyov.pancake.platform.Config;
import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.action.ActionRegistry;
import dev.kkorolyov.pancake.platform.action.MultiStageAction;
import dev.kkorolyov.pancake.platform.serialization.AutoContextualSerializer;
import dev.kkorolyov.pancake.platform.serialization.ContextualSerializer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serializes multi-stage actions.
 */
public class MultiStageActionSerializer extends ActionSerializer<MultiStageAction> {
	private static final ContextualSerializer<Action, String, ActionRegistry> autoSerializer = new AutoContextualSerializer(ActionSerializer.class);

	private final float holdThreshold = Float.parseFloat(Config.config.get("holdThreshold"));

	/**
	 * Constructs a new multi-stage action serializer.
	 */
	public MultiStageActionSerializer() {
		super(".*(,.*){2}");
	}

	@Override
	public MultiStageAction read(String out, ActionRegistry context) {
		List<Action> actions = Arrays.stream(out.split(",\\s?(?![^{]*})"))
				.map(actionS -> actionS.length() <= 0 ? null : autoSerializer.read(actionS, context))
				.collect(Collectors.toList());

		return new MultiStageAction(
				actions.get(0),
				actions.get(1),
				actions.get(2),
				holdThreshold);
	}
	@Override
	public String write(MultiStageAction in, ActionRegistry context) {
		// TODO
		return null;
	}
}
