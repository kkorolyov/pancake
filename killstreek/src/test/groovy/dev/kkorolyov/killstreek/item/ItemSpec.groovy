package dev.kkorolyov.killstreek.item

import dev.kkorolyov.pancake.component.Sprite
import dev.kkorolyov.pancake.entity.Entity

import spock.lang.Shared
import spock.lang.Specification

import java.util.function.Consumer

class ItemSpec extends Specification {
	@Shared String name = "item"
	@Shared Sprite sprite = Mock()
	@Shared int uses = 5
	@Shared Consumer<Entity> effect = {e -> e.toString()}
	Entity entity = Mock()

	Item item = new Item(name, sprite, uses)

	def "simple constructor constructs single-use item"() {
		expect:
		new Item(name, sprite).getUses() == 1
	}

	def "apply affects entity if use available"() {
		when:
		item.addEffect(effect)
		item.apply(entity)

		then:
		1 * entity.toString()
	}
	def "apply does nothing if no use available"() {
		when:
		item.addEffect(effect)
		item.setUses(0)
		item.apply(entity)

		then:
		0 * entity.toString()
	}

	def "apply returns true if use available"() {
		when:
		item.addEffect(effect)

		then:
		item.apply(entity)
	}
	def "apply returns false if no use available"() {
		when:
		item.addEffect(effect)
		item.setUses(0)

		then:
		!item.apply(entity)
	}

	def "apply decrements number of remaining uses"() {
		when:
		item.addEffect(effect)
		item.apply(entity)

		then:
		item.getUses() == uses - 1
	}

	def "apply excepts if no effects"() {
		when:
		item.apply(entity)

		then:
		thrown(NoSuchElementException)
	}
}
