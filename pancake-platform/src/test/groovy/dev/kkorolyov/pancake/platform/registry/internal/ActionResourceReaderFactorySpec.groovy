package dev.kkorolyov.pancake.platform.registry.internal

import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.registry.Registry
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton

import spock.lang.Shared
import spock.lang.Specification

import static dev.kkorolyov.pancake.platform.SpecUtilities.randKeyCode
import static dev.kkorolyov.pancake.platform.SpecUtilities.randMouseButton

class ActionResourceReaderFactorySpec extends Specification {
	@Shared
	String[] references = ["ref", "ref4", "newRef"]
	@Shared
	String[] multiStages = ["startStep", "holdStep", "endStep"]

	@Shared
	MouseButton mouseButton = randMouseButton()
	@Shared
	KeyCode keyCode = randKeyCode()
	Action keyAction = Mock()

	@Shared
	Registry<String, Action> registry = new Registry<>();

	def "reads reference"() {
		when:
		Map<String, Action> referenceToAction = references.collectEntries { [(it): Mock(Action)] }
		referenceToAction.each { key, action -> registry.put(key, action) }

		then:
		referenceToAction.each { key, action ->
			ActionResourceReaderFactory.reference(registry).convert(key).orElse(null) == action
		}
	}

	def "reads collective"() {
	}

	def "reads multi-stage"() {
	}

	def "reads key"() {
	}
}
