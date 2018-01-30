package dev.kkorolyov.killstreek.item

import dev.kkorolyov.killstreek.media.Sprite
import dev.kkorolyov.pancake.platform.action.FreeFormAction
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.entity.Signature

import spock.lang.Shared
import spock.lang.Specification

import static dev.kkorolyov.pancake.platform.SpecUtilities.randInt

class ItemSpec extends Specification {
	@Shared String name = "item"
	@Shared Sprite sprite = Mock()

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
		Set<Integer> affected = []

		when:
		item.addEffect(new FreeFormAction({ id, entities -> affected.add(id) }))
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
