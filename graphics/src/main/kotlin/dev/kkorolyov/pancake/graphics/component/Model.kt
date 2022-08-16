package dev.kkorolyov.pancake.graphics.component

import dev.kkorolyov.pancake.graphics.resource.Mesh
import dev.kkorolyov.pancake.graphics.resource.Program
import dev.kkorolyov.pancake.platform.entity.Component

/**
 * Combines a set of meshes with the program to draw them.
 */
class Model(val program: Program, vararg val meshes: Mesh) : Component
