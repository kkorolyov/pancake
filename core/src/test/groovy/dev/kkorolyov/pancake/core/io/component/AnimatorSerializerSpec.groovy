package dev.kkorolyov.pancake.core.io.component

import dev.kkorolyov.pancake.core.animation.TransformFrame
import dev.kkorolyov.pancake.core.component.Animator
import dev.kkorolyov.pancake.platform.animation.Choreography
import dev.kkorolyov.pancake.platform.animation.Timeline
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.WriteContext

import spock.lang.Specification

import java.nio.ByteBuffer

class AnimatorSerializerSpec extends Specification {
	def buffer = ByteBuffer.allocate(1024)
	def serializer = new AnimatorSerializer()

	def "serializes empty"() {
		def value = new Animator()

		when:
		serializer.write(value, new WriteContext(buffer))

		then:
		serializer.read(new ReadContext(buffer.flip())) == value
	}

	def "serializes"() {
		def choreography = new Choreography<TransformFrame>().with {
			it.put("main", new Timeline<TransformFrame>().with {
				it.put(offset, new TransformFrame().with {
					it.translation.setX(offset * 2)
					it
				})
				it.put(offset + 10, new TransformFrame().with {
					it.rotation.setY(offset * 4)
					it
				})
				it.put(offset + 100, new TransformFrame().with {
					it.scale.setZ(offset * 8)
					it
				})
				it
			})
			it.put("other", new Timeline<TransformFrame>().with {
				it.put(offset, new TransformFrame().with {
					it.scale.setX(offset * 3)
					it
				})
				it.put(offset + 20, new TransformFrame().with {
					it.rotation.setY(offset * 6)
					it
				})
				it.put(offset + 200, new TransformFrame().with {
					it.translation.setZ(offset * 9)
					it
				})
				it
			})
			it
		}
		def value = new Animator().with {
			it.put(choreography.get("main"), Animator.Type.LOOP)
			it.put(choreography.get("other"), Animator.Type.ONCE)
			it.active = false
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
