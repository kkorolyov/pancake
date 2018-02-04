package dev.kkorolyov.pancake.platform.serialization.string.action

import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.action.ActionRegistry
import dev.kkorolyov.pancake.platform.specbase.BaseSerializerSpec

import java.lang.reflect.Constructor

import static dev.kkorolyov.pancake.platform.SpecUtilities.randString

class ActionContainerStringSerializerSpec extends BaseSerializerSpec<ActionContainerStringSerializer.ActionContainer, String> {
	String name = randString()
	Action action = Mock()
	String actionS = randString()

	def setup() {
		serializer = new ActionContainerStringSerializer(new ActionRegistry())
		mockAutoSerializer([action], [actionS])

		reps << [(newActionContainer()): "$name=$actionS"]
	}

	private ActionContainerStringSerializer.ActionContainer newActionContainer() {
		Constructor<ActionContainerStringSerializer.ActionContainer> constructor = ActionContainerStringSerializer.ActionContainer.getDeclaredConstructor(ActionContainerStringSerializer, String, Action)
		constructor.setAccessible(true)

		return constructor.newInstance(serializer, name, action)
	}
}
