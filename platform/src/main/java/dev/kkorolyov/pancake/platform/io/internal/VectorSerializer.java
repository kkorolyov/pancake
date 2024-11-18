package dev.kkorolyov.pancake.platform.io.internal;

import dev.kkorolyov.pancake.platform.io.ReadContext;
import dev.kkorolyov.pancake.platform.io.Serializer;
import dev.kkorolyov.pancake.platform.io.WriteContext;
import dev.kkorolyov.pancake.platform.math.Vector2;
import dev.kkorolyov.pancake.platform.math.Vector3;

/**
 * Serializes 2D and 3D vectors.
 */
public final class VectorSerializer implements Serializer<Vector2> {
	@Override
	public void write(Vector2 value, WriteContext context) {
		// handle all subtypes
		context.putByte((byte) (value instanceof Vector3 ? 3 : 2));

		context.putDouble(value.getX());
		context.putDouble(value.getY());
		if (value instanceof Vector3 v) context.putDouble(v.getZ());
	}
	@Override
	public Vector2 read(ReadContext context) {
		var size = context.getByte();
		var result = size == 3 ? Vector3.of() : Vector2.of();

		result.setX(context.getDouble());
		result.setY(context.getDouble());
		if (result instanceof Vector3 v) v.setZ(context.getDouble());

		return result;
	}

	@Override
	public Class<Vector2> getType() {
		return Vector2.class;
	}
}
