package dev.kkorolyov.pancake.input.component

import dev.kkorolyov.pancake.input.Reaction
import dev.kkorolyov.pancake.platform.entity.Component

/**
 * Provides actions according to [delegate].
 */
class Input(private val delegate: Reaction<Any>) : Component, Reaction<Any> by delegate
