package dev.kkorolyov.pancake.platform.serialization.string.action

import dev.kkorolyov.pancake.platform.Registry
import dev.kkorolyov.pancake.platform.action.KeyAction
import dev.kkorolyov.pancake.platform.action.MultiStageAction
import dev.kkorolyov.pancake.platform.specbase.BaseSerializerSpec
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton

import static dev.kkorolyov.simplespecs.SpecUtilities.randString

class KeyActionStringSerializerSpec extends BaseSerializerSpec<KeyAction, String> {
	List<Enum> inputs = KeyCode.values() + MouseButton.values()
	MultiStageAction delegateAction = Mock()
	String delegateActionS = randString()

	def setup() {
		reps << [(new KeyAction(delegateAction, inputs)): "$inputs=$delegateActionS"]

		serializer = new KeyActionStringSerializer(Mock(Registry))
		mockAutoSerializer([(delegateAction): delegateActionS])
	}
}
