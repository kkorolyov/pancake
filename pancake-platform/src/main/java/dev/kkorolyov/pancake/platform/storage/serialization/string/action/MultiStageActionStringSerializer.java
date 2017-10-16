package dev.kkorolyov.pancake.platform.storage.serialization.string.action;

import dev.kkorolyov.pancake.platform.Config;
import dev.kkorolyov.pancake.platform.action.Action;
import dev.kkorolyov.pancake.platform.action.ActionRegistry;
import dev.kkorolyov.pancake.platform.action.MultiStageAction;
import dev.kkorolyov.pancake.platform.storage.serialization.StringSerializer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serializes multi-stage actions.
 */
public class MultiStageActionStringSerializer extends StringSerializer<MultiStageAction> {
	private final ActionRegistry registry;
	private final float holdThreshold = Float.parseFloat(Config.config.get("holdThreshold"));

	/**
	 * Constructs a new multi-stage action serializer.
	 * @param registry associated action registry
	 */
	public MultiStageActionStringSerializer(ActionRegistry registry) {
		super(".*(,.*){2}");
		this.registry = registry;
	}

	@Override
	public MultiStageAction read(String out) {
		List<Action> actions = Arrays.stream(out.split(",\\s?(?![^{]*})"))
				.map(actionS -> actionS.length() <= 0 ? null : registry.readAction(actionS))
				.collect(Collectors.toList());

		return new MultiStageAction(
				actions.get(0),
				actions.get(1),
				actions.get(2),
				holdThreshold);
	}
	@Override
	public String write(MultiStageAction in) {
		// TODO
		return null;
	}
}
