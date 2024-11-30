package dev.kkorolyov.pancake.platform.entity.io

import dev.kkorolyov.pancake.platform.SpecUtilities
import dev.kkorolyov.pancake.platform.component.DummyComponent
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.WriteContext

import spock.lang.Specification

import java.nio.ByteBuffer

class EntityPoolSerializerSpec extends Specification {
	def buffer = ByteBuffer.allocate(256)
	def serializer = new EntityPoolSerializer()

	def "serializes empty"() {
		def value = new EntityPool()

		when:
		serializer.write(value, new WriteContext(buffer))

		then:
		serializer.read(new ReadContext(buffer.flip())) == value
	}

	def "serializes"() {
		def value = new EntityPool().with {
			it.create()
			it.create().with {
				it.put(new DummyComponent(SpecUtilities.randInt()))
				it.debugNameOverride = "debuggeroo"
			}
			it.create()
			it
		}

		when:
		serializer.write(value, new WriteContext(buffer))

		then:
		serializer.read(new ReadContext(buffer.flip())) == value
	}
}
