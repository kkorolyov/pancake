package dev.kkorolyov.pancake.platform.action

import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.Signature

import spock.lang.Specification

import java.util.function.Consumer

class FreeFormActionSpec extends Specification {
	Entity entity = Mock()
	Consumer<Entity> consumer = Mock()

	FreeFormAction action = new FreeFormAction(consumer)

	def "applies consumer on entity"() {
		when:
		action.accept(entity)

		then:
		1 * entity.contains(_ as Signature) >> true
		1 * consumer.accept(entity)
	}
}
