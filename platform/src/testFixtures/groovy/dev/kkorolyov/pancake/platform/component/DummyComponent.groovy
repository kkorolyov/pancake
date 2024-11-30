package dev.kkorolyov.pancake.platform.component

import dev.kkorolyov.pancake.platform.entity.Component

final class DummyComponent implements Component {
	private final int value

	DummyComponent(int value) {
		this.value = value
	}

	int getValue() {
		return value
	}

	boolean equals(o) {
		if (this.is(o)) return true
		if (!(o instanceof DummyComponent)) return false

		DummyComponent other = (DummyComponent) o

		if (value != other.value) return false

		return true
	}
	int hashCode() {
		return value
	}
}
