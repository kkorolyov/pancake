package dev.kkorolyov.killstreek.item

import dev.kkorolyov.pancake.core.component.Sprite
import dev.kkorolyov.pancake.platform.entity.Entity

import spock.lang.Shared
import spock.lang.Specification

import java.util.function.Consumer

class ItemSpec extends Specification {
	@Shared String name = "item"
	@Shared Sprite sprite = Mock()
	@Shared Consumer<Entity> effect = {e -> e.toString()}
	Entity entity = Mock()

	Item item = new Item(name, sprite) {
		@Override
		int getMaxStackSize() {
			return 0
		}
	}

	def "apply affects"() {
		when:
		item.addEffect(effect)
		item.apply(entity)

		then:
		1 * entity.toString()
	}

	def "apply excepts if no effects"() {
		when:
		item.apply(entity)

		then:
		thrown(NoSuchElementException)
	}
}
