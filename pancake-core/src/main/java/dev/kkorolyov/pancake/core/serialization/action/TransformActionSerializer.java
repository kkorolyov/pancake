package dev.kkorolyov.pancake.core.serialization.action;

import dev.kkorolyov.pancake.core.action.TransformAction;
import dev.kkorolyov.pancake.platform.action.ActionRegistry;
import dev.kkorolyov.pancake.platform.math.Vector;
import dev.kkorolyov.pancake.platform.storage.serialization.action.ActionSerializer;
import dev.kkorolyov.pancake.platform.storage.serialization.string.NumberStringSerializer;
import dev.kkorolyov.pancake.platform.storage.serialization.string.VectorStringSerializer;

import java.math.BigDecimal;

/**
 * Serializes {@link TransformAction}.
 */
public class TransformActionSerializer extends ActionSerializer<TransformAction> {
	private static final String PREFIX = "TRANSFORM";
	private static final VectorStringSerializer positionSerializer = new VectorStringSerializer();
	private static final NumberStringSerializer rotationSerializer = new NumberStringSerializer();

	/**
	 * Constructs a new transform action serializer.
	 */
	public TransformActionSerializer() {
		super(PREFIX + "\\{" + positionSerializer.pattern() + "(,\\s?" + rotationSerializer.pattern() + ")?}");
	}

	@Override
	public TransformAction read(String out, ActionRegistry context) {
		Vector position = positionSerializer.match(out)
				.orElseThrow(IllegalStateException::new);
		float rotation = rotationSerializer.match(out)
				.orElse(BigDecimal.ZERO)
				.floatValue();

		return new TransformAction(position, rotation);
	}
	@Override
	public String write(TransformAction in, ActionRegistry context) {
		StringBuilder builder = new StringBuilder(PREFIX);
		builder.append("{").append(positionSerializer.write(in.getPosition()));

		if (in.getRotation() != null) builder.append(", ").append(rotationSerializer.write(BigDecimal.valueOf(in.getRotation())));

		return builder.append("}").toString();
	}
}
