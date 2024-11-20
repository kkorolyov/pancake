package dev.kkorolyov.pancake.core.io.component

import dev.kkorolyov.pancake.core.component.Path
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.WriteContext
import dev.kkorolyov.pancake.platform.math.Vector3

import spock.lang.Specification

import java.nio.ByteBuffer

class PathSerializerSpec extends Specification {
	def buffer = ByteBuffer.allocate(128)
	def serializer = new PathSerializer()

	def "serializes empty"() {
		def value = new Path(strength, proximity, snapStrategy)

		when:
		serializer.write(value, new WriteContext(buffer))

		then:
		serializer.read(new ReadContext(buffer.flip())) == value

		where:
		strength << [0.0d, 0.5d, 45.2d]
		proximity << [85.2d, 0.4d, 33.3d]
		snapStrategy << [Path.SnapStrategy.ALL, Path.SnapStrategy.LAST, Path.SnapStrategy.NONE]
	}

	def "serializes"() {
		def value = new Path(strength, proximity, snapStrategy).with {
			it.add(Vector3.of(strength, proximity, strength))
			it.add(Vector3.of(proximity, strength, proximity))
			it
		}

		when:
		serializer.write(value, new WriteContext(buffer))

		then:
		serializer.read(new ReadContext(buffer.flip())) == value

		where:
		strength << [0.0d, 0.5d, 45.2d]
		proximity << [85.2d, 0.4d, 33.3d]
		snapStrategy << [Path.SnapStrategy.ALL, Path.SnapStrategy.LAST, Path.SnapStrategy.NONE]
	}
}
