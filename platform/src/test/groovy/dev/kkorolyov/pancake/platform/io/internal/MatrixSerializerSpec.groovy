package dev.kkorolyov.pancake.platform.io.internal

import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.Serializer
import dev.kkorolyov.pancake.platform.io.WriteContext
import dev.kkorolyov.pancake.platform.math.Matrix2
import dev.kkorolyov.pancake.platform.math.Matrix3
import dev.kkorolyov.pancake.platform.math.Matrix4

import spock.lang.Specification

import java.nio.ByteBuffer

class MatrixSerializerSpec extends Specification {
	ByteBuffer buffer = ByteBuffer.allocate(256)
	Serializer<Matrix2> serializer = new MatrixSerializer()

	def "serializes matrix2"() {
		Matrix2 value = Matrix2.of(xx, xy, yx, yy)

		when:
		serializer.write(value, new WriteContext(buffer))

		then:
		serializer.read(new ReadContext(buffer.flip())) == value

		where:
		xx << (-4..4)
		xy << (-4..4).step(-1)
		yx << (-8..8).step(2)
		yy << (-8..8).step(-2)
	}

	def "serializes matrix3"() {
		Matrix3 value = Matrix3.of(xx, xy, xz, yx, yy, yz, zx, zy, zz)

		when:
		serializer.write(value, new WriteContext(buffer))

		then:
		serializer.read(new ReadContext(buffer.flip())) == value

		where:
		xx << (-4..4)
		xy << (-4..4).step(-1)
		xz << (-8..8).step(2)
		yx << (-8..8).step(-2)
		yy << (-4..4)
		yz << (-4..4).step(-1)
		zx << (-8..8).step(2)
		zy << (-8..8).step(-2)
		zz << (-4..4)
	}

	def "serializes matrix4"() {
		Matrix4 value = Matrix4.of(xx, xy, xz, xw, yx, yy, yz, yw, zx, zy, zz, zw, wx, wy, wz, ww)

		when:
		serializer.write(value, new WriteContext(buffer))

		then:
		serializer.read(new ReadContext(buffer.flip())) == value

		where:
		xx << (-4..4)
		xy << (-4..4).step(-1)
		xz << (-8..8).step(2)
		xw << (-8..8).step(-2)
		yx << (-4..4)
		yy << (-4..4).step(-1)
		yz << (-8..8).step(2)
		yw << (-8..8).step(-2)
		zx << (-4..4)
		zy << (-4..4)
		zz << (-4..4).step(-1)
		zw << (-8..8).step(2)
		wx << (-8..8).step(-2)
		wy << (-4..4)
		wz << (-4..4).step(-1)
		ww << (-8..8).step(2)
	}
}
