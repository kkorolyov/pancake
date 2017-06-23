package dev.kkorolyov.pancake.input

import dev.kkorolyov.simpleprops.Properties
import javafx.scene.input.KeyCode
import spock.lang.Specification

class ActionPoolSpec extends Specification {
	ActionPool actions = new ActionPool()

	def "parses full key action correctly"() {
		String keys = "[W,A,S]"

		String press = "ON_PRESS"
		String hold = "ON_HOLD"
		String release = "ON_RELEASE"

		Action pressAction = new Action(press, { e -> println press })
		Action holdAction = new Action(hold, { e -> println hold })
		Action releaseAction = new Action(release, { e -> println release })

		actions.put(pressAction)
		actions.put(holdAction)
		actions.put(releaseAction)

		Properties props = new Properties()
		props.put(keys, press, hold, release)

		expect:
		actions.parseConfig(props)[0] == new KeyAction(new Action([press, hold, release].toString(), pressAction, holdAction, releaseAction), KeyCode.W, KeyCode.A, KeyCode.S)
	}
}
