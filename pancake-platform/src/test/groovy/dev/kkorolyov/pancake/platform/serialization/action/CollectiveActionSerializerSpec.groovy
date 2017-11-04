package dev.kkorolyov.pancake.platform.serialization.action

import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.action.ActionRegistry
import dev.kkorolyov.pancake.platform.action.CollectiveAction
import dev.kkorolyov.pancake.platform.specbase.BaseSerializerSpec

import java.util.stream.Collectors

import static dev.kkorolyov.pancake.platform.SpecUtilities.randString

class CollectiveActionSerializerSpec extends BaseSerializerSpec<CollectiveAction, String> {
	List<Action> actions = (1..5).collect {Mock(Action)}
	List<String> actionsS = actions.collect {randString()}

	def setup() {
		reps << [(new CollectiveAction(actions)): actionsS.stream().collect(Collectors.joining(", ", "[", "]"))]

		serializer = new CollectiveActionSerializer(Mock(ActionRegistry))
		mockAutoSerializer(actions, actionsS)
	}
}
