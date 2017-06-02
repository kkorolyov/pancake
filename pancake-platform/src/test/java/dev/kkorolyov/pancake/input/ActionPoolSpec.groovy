package dev.kkorolyov.pancake.input

import dev.kkorolyov.pancake.Entity
import dev.kkorolyov.simpleprops.Properties
import javafx.scene.input.KeyCode
import spock.lang.Shared
import spock.lang.Specification

import java.util.function.Consumer

class ActionPoolSpec extends Specification {
	@Shared Consumer<Entity> action = { e -> e.toString() }

	ActionPool actions = new ActionPool()

	def "is case-insensitive"() {
		String mixed = "StRIng"
		String lower = mixed.toLowerCase()
		String upper = mixed.toUpperCase()

		when:
		actions.put(mixed, action)

		then:
		actions.get(mixed) == action
		actions.get(lower) == action
		actions.get(upper) == action
	}

	def "parses full key action correctly"() {
		String keys = "[W,A,S]"

		String press = "ON_PRESS"
		String hold = "ON_HOLD"
		String release = "ON_RELEASE"

		Consumer<Entity> pressAction = { e -> println press }
		Consumer<Entity> holdAction = { e -> println hold }
		Consumer<Entity> releaseAction = { e -> println release }

		actions.put(press, pressAction)
		actions.put(hold, holdAction)
		actions.put(release, releaseAction)

		Properties props = new Properties()
		props.put(keys, press, hold, release)

		expect:
		actions.parseConfig(props)[0] == new KeyAction(new Action(pressAction, holdAction, releaseAction), KeyCode.W, KeyCode.A, KeyCode.S)
	}
}
