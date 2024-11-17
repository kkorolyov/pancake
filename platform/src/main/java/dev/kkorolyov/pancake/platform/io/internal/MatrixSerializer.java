package dev.kkorolyov.pancake.platform.io.internal;

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
		if (value instanceof Matrix3) {
			context.putDouble(((Matrix3) value).getXz());
			context.putDouble(((Matrix3) value).getYz());
			context.putDouble(((Matrix3) value).getZx());
			context.putDouble(((Matrix3) value).getZy());
			context.putDouble(((Matrix3) value).getZz());
		}
		if (value instanceof Matrix4) {
			context.putDouble(((Matrix4) value).getXw());
			context.putDouble(((Matrix4) value).getYw());
			context.putDouble(((Matrix4) value).getZw());
			context.putDouble(((Matrix4) value).getWx());
			context.putDouble(((Matrix4) value).getWy());
			context.putDouble(((Matrix4) value).getWz());
			context.putDouble(((Matrix4) value).getWw());
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
		if (result instanceof Matrix3) {
			((Matrix3) result).setXz(context.getDouble());
			((Matrix3) result).setYz(context.getDouble());
			((Matrix3) result).setZx(context.getDouble());
			((Matrix3) result).setZy(context.getDouble());
			((Matrix3) result).setZz(context.getDouble());
		}
		if (result instanceof Matrix4) {
			((Matrix4) result).setXw(context.getDouble());
			((Matrix4) result).setYw(context.getDouble());
			((Matrix4) result).setZw(context.getDouble());
			((Matrix4) result).setWx(context.getDouble());
			((Matrix4) result).setWy(context.getDouble());
			((Matrix4) result).setWz(context.getDouble());
			((Matrix4) result).setWw(context.getDouble());
		}

		return result;
	}

	@Override
	public Class<Matrix2> getType() {
		return Matrix2.class;
	}
}
