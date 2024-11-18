package dev.kkorolyov.pancake.platform.animation.internal

import dev.kkorolyov.pancake.platform.animation.Timeline
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.Serializer
import dev.kkorolyov.pancake.platform.io.WriteContext
import dev.kkorolyov.pancake.test.IntFrame

import spock.lang.Specification

import java.nio.ByteBuffer

class TimelineSerializerSpec extends Specification {
	ByteBuffer buffer = ByteBuffer.allocate(256)
	Serializer<Timeline<?>> serializer = new TimelineSerializer()

	def "serializes empty"() {
		Timeline<IntFrame> value = new Timeline<>()

		when:
		serializer.write(value, new WriteContext(buffer))

		then:
		serializer.read(new ReadContext(buffer.flip())) == value
	}

	def "serializes"() {
		Timeline<IntFrame> value = new Timeline<IntFrame>().with {
			it.put(offset, new IntFrame(offset * 2))
			it.put(offset + 10, new IntFrame(offset * 4))
			it.put(offset + 100, new IntFrame(offset * 8))
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
