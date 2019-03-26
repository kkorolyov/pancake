package dev.kkorolyov.pancake.platform.serialization.string.action

import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.action.ActionRegistry
import dev.kkorolyov.pancake.platform.specbase.BaseSerializerSpec

import static dev.kkorolyov.simplespecs.SpecUtilities.randString

class ActionContainerStringSerializerSpec extends BaseSerializerSpec<Action, String> {
	String name = randString()
	Action action = Mock()
	String actionS = randString()

	def setup() {
		serializer = new ActionAssignmentStringSerializer(new ActionRegistry())
		mockAutoSerializer([
				(action): actionS
		])

		reps << [(action): "$name=$actionS"]
	}
}
