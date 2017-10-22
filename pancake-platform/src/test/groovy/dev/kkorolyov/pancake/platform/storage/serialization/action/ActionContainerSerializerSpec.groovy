package dev.kkorolyov.pancake.platform.storage.serialization.action

import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.action.ActionRegistry
import dev.kkorolyov.pancake.platform.specbase.BaseContextualSerializerSpec

import java.lang.reflect.Constructor

import static dev.kkorolyov.pancake.platform.SpecUtilities.randString
import static dev.kkorolyov.pancake.platform.SpecUtilities.setField

class ActionContainerSerializerSpec extends BaseContextualSerializerSpec<ActionContainerSerializer.ActionContainer, String, ActionRegistry> {
	String name = randString()
	Action action = Mock()
	String actionS = randString()

	def setup() {
		context = Mock(ActionRegistry)

		serializer = new ActionContainerSerializer()
		setField("autoSerializer", serializer, mockAutoContextualSerializer([action], [actionS]))

		reps << [(newActionContainer()): "$name=$actionS"]
	}

	private ActionContainerSerializer.ActionContainer newActionContainer() {
		Constructor<ActionContainerSerializer.ActionContainer> constructor = ActionContainerSerializer.ActionContainer.getDeclaredConstructor(ActionContainerSerializer, String, Action)
		constructor.setAccessible(true)

		return constructor.newInstance(serializer, name, action)
	}
}
