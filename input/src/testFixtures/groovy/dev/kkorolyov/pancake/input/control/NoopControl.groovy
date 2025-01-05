package dev.kkorolyov.pancake.input.control

import dev.kkorolyov.pancake.input.event.InputEvent
import dev.kkorolyov.pancake.platform.action.Action

class NoopControl implements Control {
	static final NoopControl INSTANCE = new NoopControl()

	@Override
	Action invoke(InputEvent event) {
		return null
	}
}
