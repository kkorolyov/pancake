package dev.kkorolyov.pancake.core.serialization.action;

import dev.kkorolyov.pancake.core.action.TransformAction;
import dev.kkorolyov.pancake.platform.math.Vector;
import dev.kkorolyov.pancake.platform.serialization.action.ActionSerializer;
import dev.kkorolyov.pancake.platform.serialization.string.NumberStringSerializer;
import dev.kkorolyov.pancake.platform.serialization.string.VectorStringSerializer;

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
		super(PREFIX + "\\{" + positionSerializer.pattern() + "(,\\s?" + rotationSerializer.pattern() + ")?}", null);
	}

	@Override
	public TransformAction read(String out) {
		Vector position = positionSerializer.match(out)
				.orElseThrow(IllegalStateException::new);
		Float rotation = rotationSerializer.match(positionSerializer.consume(out))
				.map(BigDecimal::floatValue)
				.orElse(null);

		return new TransformAction(position, rotation);
	}
	@Override
	public String write(TransformAction in) {
		StringBuilder builder = new StringBuilder(PREFIX);
		builder.append("{").append(positionSerializer.write(in.getPosition()));

		if (in.getRotation() != null) builder.append(", ").append(rotationSerializer.write(BigDecimal.valueOf(in.getRotation())));

		return builder.append("}").toString();
	}
}
