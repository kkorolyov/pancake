package dev.kkorolyov.pancake.core.serialization.action;

import dev.kkorolyov.pancake.core.action.ForceAction;
import dev.kkorolyov.pancake.platform.action.ActionRegistry;
import dev.kkorolyov.pancake.platform.storage.serialization.action.ActionSerializer;
import dev.kkorolyov.pancake.platform.storage.serialization.string.VectorStringSerializer;

/**
 * Serializes {@link ForceAction}.
 */
public class ForceActionSerializer extends ActionSerializer<ForceAction> {
	private static final String PREFIX = "FORCE";
	private static final VectorStringSerializer forceSerializer = new VectorStringSerializer();

	/**
	 * Constructs a new force action serializer.
	 */
	public ForceActionSerializer() {
		super(PREFIX + "\\{" + forceSerializer.pattern() + "}");
	}

	@Override
	public ForceAction read(String out, ActionRegistry context) {
		return new ForceAction(forceSerializer.match(out)
				.orElseThrow(IllegalStateException::new));  // Should never hit this exception
	}
	@Override
	public String write(ForceAction in, ActionRegistry context) {
		return PREFIX + "{" + forceSerializer.write(in.getForce()) + "}";
	}
}
