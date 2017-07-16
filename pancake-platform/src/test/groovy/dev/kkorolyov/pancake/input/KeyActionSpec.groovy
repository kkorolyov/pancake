package dev.kkorolyov.pancake.input

import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton

import spock.lang.Shared
import spock.lang.Specification

import static dev.kkorolyov.pancake.SpecUtilities.*

class KeyActionSpec extends Specification {
	@Shared Set<Enum<?>> keys = [KeyCode.A, KeyCode.W, KeyCode.SHIFT, MouseButton.PRIMARY]
	Action action = Mock()

	KeyAction keyAction = new KeyAction(action, keys)

	def "accepts only KeyCodes and MouseButtons"() {
		when:
		keyAction.setKeys(randKeyCode())
		then:
		notThrown(IllegalArgumentException)

		when:
		keyAction.setKeys(randMouseButton())
		then:
		notThrown(IllegalArgumentException)

		when:
		keyAction.setKeys(RandomEnum.Random)
		then:
		thrown(IllegalArgumentException)
	}

	def "signals action true when all keys given"() {
		when:
		keyAction.signal(keys, dt)

		then:
		1 * action.signal(true, dt)

		where:
		dt << randFloat()
	}
	def "signals action true when all keys and extras given"() {
		Set<Enum<?>> plusExtras = [KeyCode.F, MouseButton.SECONDARY]
		plusExtras.addAll(keys)

		when:
		keyAction.signal(plusExtras, dt)

		then:
		1 * action.signal(true, dt)

		where:
		dt << randFloat()
	}

	def "signals action false when not all keys given"() {
		Set<Enum<?>> missing = keys.toSet()
		missing.remove(keys[i])

		when:
		keyAction.signal(missing, i)

		then:
		1 * action.signal(false, i)

		where:
		i << (0..<keys.size())
	}

	enum RandomEnum {
		Random
	}
}
