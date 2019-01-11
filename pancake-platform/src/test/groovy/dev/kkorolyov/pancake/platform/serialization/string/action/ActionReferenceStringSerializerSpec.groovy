package dev.kkorolyov.pancake.platform.serialization.string.action

import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.action.ActionRegistry
import dev.kkorolyov.pancake.platform.specbase.BaseSerializerSpec

import static dev.kkorolyov.simplespecs.SpecUtilities.randString

class ActionReferenceStringSerializerSpec extends BaseSerializerSpec<Action, String> {
	def setup() {
		reps << [(Mock(Action)): randString()]

		serializer = new ActionReferenceStringSerializer(Mock(ActionRegistry) {
			get({this.&hasOut}) >> {inRep(it[0])}
			getName({this.&hasIn}) >> {outRep(it[0])}
		})
	}
}
