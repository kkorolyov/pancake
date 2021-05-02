package dev.kkorolyov.killstreek.item

import dev.kkorolyov.killstreek.media.Sprite
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.media.graphic.CompositeRenderable
import dev.kkorolyov.pancake.platform.media.graphic.Image
import dev.kkorolyov.pancake.platform.media.graphic.Viewport

import spock.lang.Shared
import spock.lang.Specification

class ItemSpec extends Specification {
	@Shared
	String name = "item"
	Sprite sprite = new Sprite(new CompositeRenderable<Image>([]), new Viewport(1, 1), 0)

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
		item.addEffect({ entity -> affected.add(entity) }, 1)
		item.apply(entity)

		then:
		affected == [entity].toSet()
	}

	def "apply excepts if no effects"() {
		when:
		item.apply(entity)

		then:
		thrown(NullPointerException)
	}
}
