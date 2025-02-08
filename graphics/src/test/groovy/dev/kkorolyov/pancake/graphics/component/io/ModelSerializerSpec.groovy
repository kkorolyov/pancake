package dev.kkorolyov.pancake.graphics.component.io

import dev.kkorolyov.pancake.graphics.component.Model
import dev.kkorolyov.pancake.graphics.resource.Mesh
import dev.kkorolyov.pancake.graphics.resource.Program
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.WriteContext
import dev.kkorolyov.pancake.platform.math.Matrix4

import spock.lang.Specification

import java.nio.ByteBuffer

class ModelSerializerSpec extends Specification {
	def buffer = ByteBuffer.allocate(256)
	def serializer = new ModelSerializer()

	def "serializes empty"() {
		def value = new Model(
				new Program(),
				[],
				null
		)

		when:
		serializer.write(value, new WriteContext(buffer))

		then:
		serializer.read(new ReadContext(buffer.flip())) == value
	}

	def "serializes"() {
		def value = new Model(
				new Program(),
				[new Mesh()],
				Matrix4.of()
		)

		when:
		serializer.write(value, new WriteContext(buffer))

		then:
		serializer.read(new ReadContext(buffer.flip())) == value
	}
}
