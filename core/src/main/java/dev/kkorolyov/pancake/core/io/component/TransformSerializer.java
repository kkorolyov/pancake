package dev.kkorolyov.pancake.core.io.component;

import dev.kkorolyov.pancake.core.component.Transform;
import dev.kkorolyov.pancake.platform.io.ReadContext;
import dev.kkorolyov.pancake.platform.io.Serializer;
import dev.kkorolyov.pancake.platform.io.WriteContext;
import dev.kkorolyov.pancake.platform.math.Matrix4;
import dev.kkorolyov.pancake.platform.math.Vector3;

/**
 * Serializes {@link Transform}s.
 */
public class TransformSerializer implements Serializer<Transform> {
	@Override
	public void write(Transform value, WriteContext context) {
		context.putObject(value.getTranslation());
		context.putObject(value.getRotation());
		context.putObject(value.getScale());
		context.putObject(value.getParent());
	}
	@Override
	public Transform read(ReadContext context) {
		var result = new Transform();

		result.getTranslation().set(context.getObject(Vector3.class));
		result.getRotation().set(context.getObject(Matrix4.class));
		result.getScale().set(context.getObject(Vector3.class));
		result.setParent(context.getObject(Transform.class));

		return result;
	}

	@Override
	public Class<Transform> getType() {
		return Transform.class;
	}
}
