package dev.kkorolyov.pancake.graphics.resource

import java.io.InputStream

/**
 * A step in a shader program.
 */
class Shader(type: Type = Type.VERTEX, source: String = "") : RenderResource() {
	constructor(type: Type, source: InputStream) : this(type, String(source.readAllBytes()))

	var type: Type = type
		set(value) {
			field = value
			invalidate()
		}
	var source: String = source
		set(value) {
			field = value
			invalidate()
		}

	enum class Type {
		VERTEX,
		FRAGMENT
	}
}
