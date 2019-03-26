package dev.kkorolyov.killstreek.item

import dev.kkorolyov.killstreek.media.Sprite
import dev.kkorolyov.pancake.platform.entity.Entity

import spock.lang.Shared
import spock.lang.Specification

class ItemSpec extends Specification {
	@Shared
	String name = "item"
	@Shared
	Sprite sprite = Mock()
	@Shared
	Entity entity = Mock()

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
		item.apply(entity)

		then:
		affected == [entity].toSet()
	}

	def "apply excepts if no effects"() {
		when:
		item.apply(entity)

		then:
		thrown(NoSuchElementException)
	}
}
