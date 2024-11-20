package dev.kkorolyov.pancake.core.io;

import dev.kkorolyov.pancake.core.animation.TransformFrame;
import dev.kkorolyov.pancake.platform.io.ReadContext;
import dev.kkorolyov.pancake.platform.io.Serializer;
import dev.kkorolyov.pancake.platform.io.WriteContext;
import dev.kkorolyov.pancake.platform.math.Vector3;

/**
 * Serializes {@link TransformFrame}s.
 */
public final class TransformFrameSerializer implements Serializer<TransformFrame> {
	@Override
	public void write(TransformFrame value, WriteContext context) {
		context.putObject(value.getTranslation());
		context.putObject(value.getRotation());
		context.putObject(value.getScale());
	}
	@Override
	public TransformFrame read(ReadContext context) {
		var result = new TransformFrame();

		result.getTranslation().set(context.getObject(Vector3.class));
		result.getRotation().set(context.getObject(Vector3.class));
		result.getScale().set(context.getObject(Vector3.class));

		return result;
	}

	@Override
	public Class<TransformFrame> getType() {
		return TransformFrame.class;
	}
}
