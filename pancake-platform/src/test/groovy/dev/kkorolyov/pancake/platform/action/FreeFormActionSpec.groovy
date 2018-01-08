package dev.kkorolyov.pancake.platform.action

import dev.kkorolyov.pancake.platform.entity.EntityPool

import java.util.function.BiConsumer

class FreeFormActionSpec extends ActionSpec {
	BiConsumer<UUID, EntityPool> consumer = Mock()

	@Override
	FreeFormAction initAction() {
		return new FreeFormAction(consumer)
	}

	def "applies consumer on entity"() {
		when:
		action.accept(id, entities)

		then:
		1 * entities.contains(id, signature) >> true
		1 * consumer.accept(id, entities)
	}
}
