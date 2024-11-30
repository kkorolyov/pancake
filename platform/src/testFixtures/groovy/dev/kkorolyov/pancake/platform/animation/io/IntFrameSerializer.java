package dev.kkorolyov.pancake.platform.animation.io;

import dev.kkorolyov.pancake.platform.animation.IntFrame;
import dev.kkorolyov.pancake.platform.io.ReadContext;
import dev.kkorolyov.pancake.platform.io.Serializer;
import dev.kkorolyov.pancake.platform.io.WriteContext;

/**
 * Serializes {@link IntFrame}s.
 */
public final class IntFrameSerializer implements Serializer<IntFrame> {
	@Override
	public void write(IntFrame value, WriteContext context) {
		context.putInt(value.getValue());
	}
	@Override
	public IntFrame read(ReadContext context) {
		return new IntFrame(context.getInt());
	}

	@Override
	public Class<IntFrame> getType() {
		return IntFrame.class;
	}
}
