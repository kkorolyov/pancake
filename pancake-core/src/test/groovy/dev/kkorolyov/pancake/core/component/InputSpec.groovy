package dev.kkorolyov.pancake.core.component

import dev.kkorolyov.pancake.platform.Resources
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.action.MultiStageAction
import dev.kkorolyov.pancake.platform.application.Application
import dev.kkorolyov.pancake.platform.registry.Registry
import dev.kkorolyov.simpleprops.Properties
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton

import spock.lang.Specification

import static dev.kkorolyov.pancake.platform.SpecUtilities.randKeyCode
import static dev.kkorolyov.pancake.platform.SpecUtilities.randMouseButton
import static dev.kkorolyov.simplespecs.SpecUtilities.randString
import static dev.kkorolyov.simplespecs.SpecUtilities.setField

class InputSpec extends Specification {
	String name = randString()
	Action action = Mock()

	MouseButton mouseButton = randMouseButton()
	KeyCode keyCode = randKeyCode()

	Properties props = new Properties()
	Registry<String, Action> registry = new Registry<>()

	def setup() {
		setField("APPLICATION", Resources, Mock(Application) {
			toInput(mouseButton.name()) >> mouseButton
			toInput(keyCode.name()) >> keyCode
		})

		props.put(name, "($mouseButton, $keyCode)")
		registry.put(name, action)
	}

	def "reads handlers from props"() {
		expect:
		Input.Handler.fromProperties(props, registry) == [new Input.Handler(
				new MultiStageAction(action, null, null, 0),
				[mouseButton, keyCode]
		)] as Set
	}
}
