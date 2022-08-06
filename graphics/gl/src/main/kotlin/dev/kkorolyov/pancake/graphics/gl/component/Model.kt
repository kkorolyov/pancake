package dev.kkorolyov.pancake.graphics.gl.component

import dev.kkorolyov.pancake.graphics.gl.Mesh
import dev.kkorolyov.pancake.graphics.gl.Program
import dev.kkorolyov.pancake.platform.entity.Component

/**
 * Combines a set of common meshes with the program to draw them.
 */
class Model(val program: Program, vararg val meshes: Mesh) : Component
