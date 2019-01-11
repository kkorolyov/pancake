package dev.kkorolyov.killstreek.item

import dev.kkorolyov.killstreek.media.Sprite
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.entity.Signature

import spock.lang.Shared
import spock.lang.Specification

class ItemSpec extends Specification {
	@Shared
	String name = "item"
	@Shared
	Sprite sprite = Mock()

	int id = randInt()
	EntityPool entities = Mock() {
		contains(id, new Signature()) >> true
	}

	Item item = new Item(name, sprite) {
		@Override
		int getMaxStackSize() {
			return 0
		}
	}

	def "apply affects"() {
		Set<Entity> affected = []

		when:
		item.addEffect({ entity -> affected.add(entity) })
		item.apply(id, entities)

		then:
		affected == [id].toSet()
	}

	def "apply excepts if no effects"() {
		when:
		item.apply(id, entities)

		then:
		thrown(NoSuchElementException)
	}
}
