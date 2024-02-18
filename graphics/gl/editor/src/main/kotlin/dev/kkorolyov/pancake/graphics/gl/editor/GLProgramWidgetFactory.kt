package dev.kkorolyov.pancake.graphics.gl.editor

import dev.kkorolyov.pancake.editor.FileAccess
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.factory.WidgetFactory
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.sameLine
import dev.kkorolyov.pancake.editor.separator
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.graphics.gl.resource.GLProgram
import dev.kkorolyov.pancake.graphics.gl.resource.GLShader
import dev.kkorolyov.pancake.graphics.resource.Program
import dev.kkorolyov.pancake.platform.io.Resources
import imgui.flag.ImGuiTableFlags

private fun shortPath(path: String) = path.split("""[/\\]""".toRegex()).last()

class GLProgramWidgetFactory : WidgetFactory<Program> {
	override val type = Program::class.java

	override fun get(t: Program): Widget? {
		TODO("Not yet implemented")
	}

	override fun get(c: Class<out Program>, onNew: (Program) -> Unit): Widget? = WidgetFactory.get<GLProgram>(c, onNew) {
		val shaders = mutableMapOf<String, GLShader.Type>()
		val uniforms = mutableMapOf<Int, Any>()

		var newPath = ""
		var newType = GLShader.Type.VERTEX

		Widget {
			table("##shaders", 3, flags = ImGuiTableFlags.SizingStretchProp) {
				if (shaders.isNotEmpty()) {
					var toRemove: String? = null

					shaders.forEach { path, type ->
						column {
							text(shortPath(path))
							tooltip(path)
						}
						column { text(type) }
						column {
							button("-") { toRemove = path }
						}
					}

					// augment elements only after done iterating
					toRemove?.let(shaders::remove)

					for (i in (1..3)) {
						column { separator() }
					}
				}

				// to add new values
				column {
					if (newPath.isNotEmpty()) {
						text(shortPath(newPath))
						tooltip(newPath)

						sameLine()
					}
					button("select") {
						FileAccess.pickFile(FileAccess.Filter("GLSL Shader", "glsl", "vert", "frag"))?.let { newPath = it }
					}
				}
				column {
					input("##type", newType) { newType = it }
				}
				column {
					if (newPath.isNotEmpty() && newPath !in shaders) {
						button("+") {
							shaders[newPath] = newType
							newPath = ""
						}
					}
				}
			}

			button("apply") {
				onNew(GLProgram(
					*shaders
						.map { (path, type) ->
							Resources.inStream(path).use { GLShader(type, it) }
						}
						.toTypedArray(), uniforms = uniforms
				))
			}
		}
	}
}
