package dev.kkorolyov.pancake.core.serialization.string.action;

import dev.kkorolyov.pancake.core.action.ForceAction;
import dev.kkorolyov.pancake.platform.serialization.string.VectorStringSerializer;
import dev.kkorolyov.pancake.platform.serialization.string.action.ActionStringSerializer;

/**
 * Serializes {@link ForceAction}.
 */
public class ForceActionSerializer extends ActionStringSerializer<ForceAction> {
	private static final String PREFIX = "FORCE";
	private static final VectorStringSerializer forceSerializer = new VectorStringSerializer();

	/**
	 * Constructs a new force action serializer.
	 */
	public ForceActionSerializer() {
		super(PREFIX + "\\{" + forceSerializer.pattern() + "}");
	}

	@Override
	public ForceAction read(String out) {
		return new ForceAction(forceSerializer.match(out)
				.orElseThrow(IllegalStateException::new));  // Should never hit this exception
	}
	@Override
	public String write(ForceAction in) {
		return PREFIX + "{" + forceSerializer.write(in.getForce()) + "}";
	}
}
