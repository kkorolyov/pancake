package dev.kkorolyov.pancake.core.serialization.string.action;

import dev.kkorolyov.pancake.core.action.TransformAction;
import dev.kkorolyov.pancake.platform.math.Vector;
import dev.kkorolyov.pancake.platform.serialization.string.VectorStringSerializer;
import dev.kkorolyov.pancake.platform.serialization.string.action.ActionStringSerializer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serializes {@link TransformAction}.
 */
public class TransformActionSerializer extends ActionStringSerializer<TransformAction> {
	private static final String PREFIX = "TRANSFORM";
	private static final VectorStringSerializer vectorSerializer = new VectorStringSerializer();

	/**
	 * Constructs a new transform action serializer.
	 */
	public TransformActionSerializer() {
		super(PREFIX + "\\{" + vectorSerializer.pattern() + "(,\\s?" + vectorSerializer.pattern() + ")?}");
	}

	@Override
	public TransformAction read(String out) {
		List<Vector> vectors = vectorSerializer.matches(out)
				.collect(Collectors.toList());

		Vector position = vectors.get(0);
		Vector rotation = vectors.size() > 1 ? vectors.get(1) : null;

		return new TransformAction(position, rotation);
	}
	@Override
	public String write(TransformAction in) {
		StringBuilder builder = new StringBuilder(PREFIX);
		builder.append("{").append(vectorSerializer.write(in.getPosition()));

		if (in.getRotation() != null) builder.append(", ").append(vectorSerializer.write(in.getRotation()));

		return builder.append("}").toString();
	}
}
