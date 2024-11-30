package dev.kkorolyov.pancake.platform.animation.io

import dev.kkorolyov.pancake.platform.animation.Choreography
import dev.kkorolyov.pancake.platform.animation.Timeline
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.WriteContext
import dev.kkorolyov.pancake.platform.animation.IntFrame

import spock.lang.Specification

import java.nio.ByteBuffer

class ChoreographySerializerSpec extends Specification {
	def buffer = ByteBuffer.allocate(512)
	def serializer = new ChoreographySerializer()

	def "serializes empty"() {
		def value = new Choreography<>()

		when:
		serializer.write(value, new WriteContext(buffer))

		then:
		serializer.read(new ReadContext(buffer.flip())) == value
	}

	def "serializes"() {
		def value = new Choreography<IntFrame>().with {
			it.put("main", new Timeline<IntFrame>().with {
				it.put(offset, new IntFrame(offset * 2))
				it.put(offset + 10, new IntFrame(offset * 4))
				it.put(offset + 100, new IntFrame(offset * 8))
				it
			})
			it.put("other", new Timeline<IntFrame>().with {
				it.put(offset, new IntFrame(offset * 3))
				it.put(offset + 20, new IntFrame(offset * 6))
				it.put(offset + 200, new IntFrame(offset * 9))
				it
			})
			it
		}

		when:
		serializer.write(value, new WriteContext(buffer))

		then:
		serializer.read(new ReadContext(buffer.flip())) == value

		where:
		offset << (0..4)
	}
}
