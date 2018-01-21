package dev.kkorolyov.killstreek.item

import dev.kkorolyov.killstreek.media.Sprite
import dev.kkorolyov.pancake.platform.entity.EntityPool

import spock.lang.Shared
import spock.lang.Specification

import static dev.kkorolyov.pancake.platform.SpecUtilities.randInt

class ItemSpec extends Specification {
	@Shared String name = "item"
	@Shared Sprite sprite = Mock()

	int id = randInt()
	EntityPool entities = Mock()

	Item item = new Item(name, sprite) {
		@Override
		int getMaxStackSize() {
			return 0
		}
	}

	def "apply affects"() {
		Set<Integer> affected = []

		when:
		item.addEffect({ id, entities -> affected.add(id) })
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
