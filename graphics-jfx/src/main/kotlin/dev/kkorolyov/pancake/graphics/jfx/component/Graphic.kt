package dev.kkorolyov.pancake.graphics.jfx.component

import dev.kkorolyov.pancake.graphics.jfx.drawable.Drawable
import dev.kkorolyov.pancake.platform.entity.Component

/**
 * A [Drawable] component.
 */
class Graphic(private val delegate: Drawable) : Component, Drawable by delegate
