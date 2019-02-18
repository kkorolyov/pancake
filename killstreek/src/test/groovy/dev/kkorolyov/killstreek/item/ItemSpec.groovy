package dev.kkorolyov.killstreek.item

import dev.kkorolyov.killstreek.media.Sprite
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool
import dev.kkorolyov.pancake.platform.event.management.ManagedEventBroadcaster

import spock.lang.Shared
import spock.lang.Specification

import static dev.kkorolyov.simplespecs.SpecUtilities.randInt

class ItemSpec extends Specification {
	@Shared
	String name = "item"
	@Shared
	Sprite sprite = Mock()

	int id = randInt()
	Entity entity = new EntityPool(new ManagedEventBroadcaster())
			.create()

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
