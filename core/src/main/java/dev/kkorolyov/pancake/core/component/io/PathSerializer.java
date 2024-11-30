package dev.kkorolyov.pancake.core.component.io;

import dev.kkorolyov.pancake.core.component.Path;
import dev.kkorolyov.pancake.platform.io.ReadContext;
import dev.kkorolyov.pancake.platform.io.Serializer;
import dev.kkorolyov.pancake.platform.io.WriteContext;
import dev.kkorolyov.pancake.platform.math.Vector3;

/**
 * Serializes {@link Path}s.
 */
public final class PathSerializer implements Serializer<Path> {
	@Override
	public void write(Path value, WriteContext context) {
		context.putDouble(value.getStrength());
		context.putDouble(value.getProximity());
		context.putString(value.getSnapStrategy().name());

		context.putInt(value.size());
		for (Vector3 step : value) {
			context.putObject(step);
		}
	}
	@Override
	public Path read(ReadContext context) {
		var result = new Path(context.getDouble(), context.getDouble(), Path.SnapStrategy.valueOf(context.getString()));

		var size = context.getInt();
		for (int i = 0; i < size; i++) {
			result.add(context.getObject(Vector3.class));
		}

		return result;
	}

	@Override
	public Class<Path> getType() {
		return Path.class;
	}
}
