package dev.kkorolyov.pancake.core.io.component;

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
			context.putString(entry.getValue().type().name());
			context.putInt(entry.getValue().playback().getOffset());
		}
	}
	@Override
	public Animator read(ReadContext context) {
		var result = new Animator();
		result.setActive(context.getBoolean());

		var size = context.getInt();
		for (int i = 0; i < size; i++) {
			result.put(
					context.getObject(Choreography.class).get(context.getString()),
					Animator.Type.valueOf(context.getString()),
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
