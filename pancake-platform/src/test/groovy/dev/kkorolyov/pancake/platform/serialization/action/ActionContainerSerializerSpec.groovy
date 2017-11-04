package dev.kkorolyov.pancake.platform.serialization.action

import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.action.ActionRegistry
import dev.kkorolyov.pancake.platform.specbase.BaseSerializerSpec

import java.lang.reflect.Constructor

import static dev.kkorolyov.pancake.platform.SpecUtilities.randString

class ActionContainerSerializerSpec extends BaseSerializerSpec<ActionContainerSerializer.ActionContainer, String> {
	String name = randString()
	Action action = Mock()
	String actionS = randString()

	def setup() {
		serializer = new ActionContainerSerializer(new ActionRegistry())
		mockAutoSerializer([action], [actionS])

		reps << [(newActionContainer()): "$name=$actionS"]
	}

	private ActionContainerSerializer.ActionContainer newActionContainer() {
		Constructor<ActionContainerSerializer.ActionContainer> constructor = ActionContainerSerializer.ActionContainer.getDeclaredConstructor(ActionContainerSerializer, String, Action)
		constructor.setAccessible(true)

		return constructor.newInstance(serializer, name, action)
	}
}
