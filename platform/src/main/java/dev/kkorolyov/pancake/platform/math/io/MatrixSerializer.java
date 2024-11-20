package dev.kkorolyov.pancake.platform.math.io;

import dev.kkorolyov.pancake.platform.io.ReadContext;
import dev.kkorolyov.pancake.platform.io.Serializer;
import dev.kkorolyov.pancake.platform.io.WriteContext;
import dev.kkorolyov.pancake.platform.math.Matrix2;
import dev.kkorolyov.pancake.platform.math.Matrix3;
import dev.kkorolyov.pancake.platform.math.Matrix4;

/**
 * Serializes 2D, 3D, and 4D matrices.
 */
public final class MatrixSerializer implements Serializer<Matrix2> {
	@Override
	public void write(Matrix2 value, WriteContext context) {
		// handle all subtypes
		context.putByte((byte) (value instanceof Matrix4 ? 4 : value instanceof Matrix3 ? 3 : 2));

		context.putDouble(value.getXx());
		context.putDouble(value.getXy());
		context.putDouble(value.getYx());
		context.putDouble(value.getYy());
		if (value instanceof Matrix3 v) {
			context.putDouble(v.getXz());
			context.putDouble(v.getYz());
			context.putDouble(v.getZx());
			context.putDouble(v.getZy());
			context.putDouble(v.getZz());
		}
		if (value instanceof Matrix4 v) {
			context.putDouble(v.getXw());
			context.putDouble(v.getYw());
			context.putDouble(v.getZw());
			context.putDouble(v.getWx());
			context.putDouble(v.getWy());
			context.putDouble(v.getWz());
			context.putDouble(v.getWw());
		}
	}
	@Override
	public Matrix2 read(ReadContext context) {
		var size = context.getByte();
		var result = size == 4 ? Matrix4.of() : size == 3 ? Matrix3.of() : Matrix2.of();

		result.setXx(context.getDouble());
		result.setXy(context.getDouble());
		result.setYx(context.getDouble());
		result.setYy(context.getDouble());
		if (result instanceof Matrix3 v) {
			v.setXz(context.getDouble());
			v.setYz(context.getDouble());
			v.setZx(context.getDouble());
			v.setZy(context.getDouble());
			v.setZz(context.getDouble());
		}
		if (result instanceof Matrix4 v) {
			v.setXw(context.getDouble());
			v.setYw(context.getDouble());
			v.setZw(context.getDouble());
			v.setWx(context.getDouble());
			v.setWy(context.getDouble());
			v.setWz(context.getDouble());
			v.setWw(context.getDouble());
		}

		return result;
	}

	@Override
	public Class<Matrix2> getType() {
		return Matrix2.class;
	}
}
