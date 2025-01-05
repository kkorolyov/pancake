package dev.kkorolyov.pancake.input.control.io

import dev.kkorolyov.pancake.input.control.MouseButtonControl
import dev.kkorolyov.pancake.input.event.StateEvent
import dev.kkorolyov.pancake.platform.action.Action
import dev.kkorolyov.pancake.platform.io.ReadContext
import dev.kkorolyov.pancake.platform.io.Serializer
import dev.kkorolyov.pancake.platform.io.WriteContext

/**
 * Serializes [MouseButtonControl]s.
 */
class MouseButtonControlSerializer : Serializer<MouseButtonControl> {
	override fun write(value: MouseButtonControl, context: WriteContext) {
		context.putInt(value.button)
		context.putInt(value.state.ordinal)
		context.putString(value.action.javaClass.name)
		context.putObject(value.action)
	}

	override fun read(context: ReadContext): MouseButtonControl = MouseButtonControl(
		context.int,
		StateEvent.State.entries[context.int],
		context.getObject(Class.forName(context.string) as Class<Action>)
	)

	override fun getType(): Class<MouseButtonControl> = MouseButtonControl::class.java
}
