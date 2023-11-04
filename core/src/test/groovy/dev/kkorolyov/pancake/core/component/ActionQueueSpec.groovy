package dev.kkorolyov.pancake.core.component

import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.entity.Entity
import dev.kkorolyov.pancake.platform.entity.EntityPool

import spock.lang.Specification

class ActionQueueSpec extends Specification {
	EntityPool entities = new EntityPool()
	Entity entity = entities.create()

	ActionQueue actionQueue = new ActionQueue()

	def "iterates added actions in order"() {
		Action a1 = Mock()
		Action a2 = Mock()

		when:
		actionQueue.add(a2)
		actionQueue.add(a1)

		then:
		actionQueue.toList() == [a2, a1]
	}

	def "buffers adding actions between forEach and flush"() {
		Action a1 = Mock()
		Action a2 = Mock()
		Action a3 = Mock {
			apply(_) >> { actionQueue.add(a1) }
		}

		when:
		actionQueue.add(a3)
		actionQueue.forEach { it.apply(entity) }
		actionQueue.add(a2)

		then:
		actionQueue.toList() == [a3]
	}
	def "adds buffered actions after flush"() {
		Action a1 = Mock()
		Action a2 = Mock()
		Action a3 = Mock {
			apply(_) >> { actionQueue.add(a1) }
		}

		when:
		actionQueue.add(a3)
		actionQueue.forEach { it.apply(entity) }
		actionQueue.add(a2)
		actionQueue.flush(0)

		then:
		actionQueue.toList() == [a1, a2]
	}

	def "adds delayed actions on flush after elapsed dt"() {
		Action soon = Mock()
		Action later = Mock()
		Action notNow = Mock()

		when:
		actionQueue.delay(later, 6)
		actionQueue.delay(soon, 2)
		actionQueue.delay(notNow, 7)
		actionQueue.flush(6)

		then:
		actionQueue.toList() == [soon, later]
	}
	def "adds subsequent delayed actions with shift"() {
		Action soon = Mock()
		Action sortaLater = Mock()

		when:
		actionQueue.delay(soon, 3)
		actionQueue.flush(2)
		actionQueue.delay(sortaLater, 2)
		actionQueue.flush(2)

		then:
		actionQueue.toList() == [soon, sortaLater]
	}
}
