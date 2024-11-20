package dev.kkorolyov.pancake.platform.animation.io;

import dev.kkorolyov.pancake.platform.animation.Frame;
import dev.kkorolyov.pancake.platform.animation.Timeline;
import dev.kkorolyov.pancake.platform.io.ReadContext;
import dev.kkorolyov.pancake.platform.io.Serializer;
import dev.kkorolyov.pancake.platform.io.WriteContext;

import java.util.Map;
import java.util.stream.StreamSupport;

/**
 * Serializes {@link Timeline}s.
 */
public final class TimelineSerializer implements Serializer<Timeline<?>> {
	@Override
	public void write(Timeline<?> value, WriteContext context) {
		var entries = StreamSupport.stream(value.spliterator(), false).toList();
		context.putInt(entries.size());
		for (Map.Entry<Integer, ?> entry : entries) {
			context.putInt(entry.getKey());
			context.putString(entry.getValue().getClass().getName());
			context.putObject(entry.getValue());
		}
	}
	@Override
	public Timeline<?> read(ReadContext context) {
		var result = new Timeline();

		var size = context.getInt();
		for (int i = 0; i < size; i++) {
			try {
				result.put(context.getInt(), (Frame) context.getObject(Class.forName(context.getString())));
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException("cannot read serialized timeline frame", e);
			}
		}

		return result;
	}

	@Override
	public Class<Timeline<?>> getType() {
		return (Class) Timeline.class;
	}
}
