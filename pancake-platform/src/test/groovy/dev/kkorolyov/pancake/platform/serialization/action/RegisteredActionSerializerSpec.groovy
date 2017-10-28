package dev.kkorolyov.pancake.platform.serialization.action

import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.action.ActionRegistry
import dev.kkorolyov.pancake.platform.specbase.BaseContextualSerializerSpec

import static dev.kkorolyov.pancake.platform.SpecUtilities.randString

class RegisteredActionSerializerSpec extends BaseContextualSerializerSpec<Action, String, ActionRegistry> {
	def setup() {
		reps << [(Mock(Action)): randString()]
		context = Mock(ActionRegistry) {
			get({this.&hasOut}) >> {inRep(it[0])}
			getName({this.&hasIn}) >> {outRep(it[0])}
		}
		serializer = new RegisteredActionSerializer()
	}
}
