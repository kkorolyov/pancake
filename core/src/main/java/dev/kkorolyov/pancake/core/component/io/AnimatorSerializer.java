package dev.kkorolyov.pancake.core.component.io;

import dev.kkorolyov.pancake.core.animation.TransformFrame;
import dev.kkorolyov.pancake.core.component.Animator;
import dev.kkorolyov.pancake.platform.animation.Choreography;
import dev.kkorolyov.pancake.platform.io.ReadContext;
import dev.kkorolyov.pancake.platform.io.Serializer;
import dev.kkorolyov.pancake.platform.io.WriteContext;

import java.util.Map;

/**
 * Serializes {@link Animator}s.
 */
public final class AnimatorSerializer implements Serializer<Animator> {
	@Override
	public void write(Animator value, WriteContext context) {
		context.putBoolean(value.isActive());

		context.putInt(value.size());
		for (Map.Entry<Choreography.Role<TransformFrame>, Animator.PlaybackConfig<TransformFrame>> entry : value) {
			context.putObject(entry.getKey().choreography());
			context.putString(entry.getKey().key());
			context.putInt(entry.getValue().type().ordinal());
			context.putInt(entry.getValue().playback().getOffset());
		}
	}
	@Override
	public Animator read(ReadContext context) {
		var result = new Animator();
		result.setActive(context.getBoolean());

		var size = context.getInt();
		var types = Animator.Type.values();
		for (int i = 0; i < size; i++) {
			result.put(
					context.getObject(Choreography.class).get(context.getString()),
					types[context.getInt()],
					context.getInt()
			);
		}

		return result;
	}

	@Override
	public Class<Animator> getType() {
		return Animator.class;
	}
}
