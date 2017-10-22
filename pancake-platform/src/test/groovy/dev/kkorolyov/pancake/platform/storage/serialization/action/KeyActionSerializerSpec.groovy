package dev.kkorolyov.pancake.platform.storage.serialization.action

import dev.kkorolyov.pancake.platform.action.ActionRegistry
import dev.kkorolyov.pancake.platform.action.KeyAction
import dev.kkorolyov.pancake.platform.action.MultiStageAction
import dev.kkorolyov.pancake.platform.specbase.BaseContextualSerializerSpec
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton

import static dev.kkorolyov.pancake.platform.SpecUtilities.randString

class KeyActionSerializerSpec extends BaseContextualSerializerSpec<KeyAction, String, ActionRegistry> {
	List<Enum> inputs = KeyCode.values() + MouseButton.values()
	MultiStageAction delegate = Mock()
	String delegateS = randString()

	def setup() {
		reps << [(new KeyAction(delegate, inputs)): "$inputs = $delegateS"]
		context = Mock(ActionRegistry) {
			get({this.&hasOut}) >> {inRep(it[0])}
			getName({this.&hasIn}) >> {outRep(it[0])}
		}
		serializer = new KeyActionSerializer()
	}
}
