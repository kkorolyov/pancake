package dev.kkorolyov.pancake.platform.storage.serialization.action

import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.action.ActionRegistry
import dev.kkorolyov.pancake.platform.action.CollectiveAction
import dev.kkorolyov.pancake.platform.specbase.BaseContextualSerializerSpec

import java.util.stream.Collectors

import static dev.kkorolyov.pancake.platform.SpecUtilities.randString
import static dev.kkorolyov.pancake.platform.SpecUtilities.setField

class CollectiveActionSerializerSpec extends BaseContextualSerializerSpec<CollectiveAction, String, ActionRegistry> {
	List<Action> actions = (1..5).collect {Mock(Action)}
	List<String> actionsS = actions.collect {randString()}

	def setup() {
		reps << [(new CollectiveAction(actions)): actionsS.stream().collect(Collectors.joining(", ", "{", "}"))]
		context = Mock(ActionRegistry)

		serializer = new CollectiveActionSerializer()
		setField("autoSerializer", serializer.class, null, mockAutoContextualSerializer(actions, actionsS))
	}
}
