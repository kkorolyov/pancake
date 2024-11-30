package dev.kkorolyov.pancake.core.component.limit.io;

import dev.kkorolyov.pancake.core.component.limit.TransformLimit;
import dev.kkorolyov.pancake.platform.io.ReadContext;
import dev.kkorolyov.pancake.platform.io.Serializer;
import dev.kkorolyov.pancake.platform.io.WriteContext;
import dev.kkorolyov.pancake.platform.math.Vector3;

import java.util.Objects;

/**
 * Serializes {@link TransformLimit}s.
 */
public final class TransformLimitSerializer implements Serializer<TransformLimit> {
	@Override
	public void write(TransformLimit value, WriteContext context) {
		context.putObject(value.getTranslationMin());
		context.putObject(value.getTranslationMax());
		context.putObject(value.getScaleMin());
		context.putObject(value.getScaleMax());
	}
	@Override
	public TransformLimit read(ReadContext context) {
		var result = new TransformLimit();

		result.getTranslationMin().set(Objects.requireNonNull(context.getObject(Vector3.class)));
		result.getTranslationMax().set(Objects.requireNonNull(context.getObject(Vector3.class)));
		result.getScaleMin().set(Objects.requireNonNull(context.getObject(Vector3.class)));
		result.getScaleMax().set(Objects.requireNonNull(context.getObject(Vector3.class)));

		return result;
	}

	@Override
	public Class<TransformLimit> getType() {
		return TransformLimit.class;
	}
}
