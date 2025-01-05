package dev.kkorolyov.pancake.input.control.io

import dev.kkorolyov.pancake.input.control.KeyControl
import dev.kkorolyov.pancake.input.event.StateEvent
import dev.kkorolyov.pancake.platform.Registry
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.Serializer
import dev.kkorolyov.pancake.platform.io.WriteContext

/**
 * Serializes [KeyControl]s.
 */
class KeyControlSerializer : Serializer<KeyControl> {
	override fun write(value: KeyControl, context: WriteContext) {
		context.putInt(value.scanCode)
		context.putInt(value.state.ordinal)
		context.putString(Registry.get(Action::class.java).lookup(value.action))
	}

	override fun read(context: ReadContext): KeyControl = KeyControl(
		context.int,
		StateEvent.State.entries[context.int],
		Registry.get(Action::class.java).get(context.string)
	)

	override fun getType(): Class<KeyControl> = KeyControl::class.java
}
