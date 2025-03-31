package dev.kkorolyov.pancake.graphics.editor

import dev.kkorolyov.pancake.editor.DebouncedValue
import dev.kkorolyov.pancake.editor.FileAccess
import dev.kkorolyov.pancake.editor.Widget
import dev.kkorolyov.pancake.editor.button
import dev.kkorolyov.pancake.editor.factory.WidgetFactory
import dev.kkorolyov.pancake.editor.header
import dev.kkorolyov.pancake.editor.image
import dev.kkorolyov.pancake.editor.input
import dev.kkorolyov.pancake.editor.list
import dev.kkorolyov.pancake.editor.table
import dev.kkorolyov.pancake.editor.text
import dev.kkorolyov.pancake.editor.tooltip
import dev.kkorolyov.pancake.graphics.PixelBuffer
import dev.kkorolyov.pancake.graphics.component.Model
import dev.kkorolyov.pancake.graphics.renderBackend
import dev.kkorolyov.pancake.graphics.resource.Mesh
import dev.kkorolyov.pancake.graphics.resource.Program
import dev.kkorolyov.pancake.graphics.resource.Shader
import dev.kkorolyov.pancake.graphics.resource.Texture
import dev.kkorolyov.pancake.platform.entity.Component
import dev.kkorolyov.pancake.platform.math.Matrix4
import dev.kkorolyov.pancake.platform.math.Vector3
import imgui.flag.ImGuiTableFlags
import imgui.flag.ImGuiTreeNodeFlags
import org.lwjgl.system.MemoryUtil
import kotlin.math.max

// width and height of the snapshot viewport
private const val RESOLUTION = 512

private const val IMAGE_WIDTH = 128
private const val IMAGE_HEIGHT = 128

private val transform = Matrix4.of()
private val program by lazy {
	Program(
		Shader(Shader.Type.VERTEX, Shader.Source.Path("dev/kkorolyov/pancake/graphics/editor/shaders/image.vert")),
		Shader(Shader.Type.FRAGMENT, Shader.Source.Path("dev/kkorolyov/pancake/graphics/editor/shaders/image.frag"))
	).apply {
		uniforms[0] = transform
	}
}
private val texture = Texture {
	PixelBuffer(RESOLUTION, RESOLUTION, 0, 4, MemoryUtil.memCalloc(RESOLUTION * RESOLUTION * 4), MemoryUtil::memFree)
}

private val meshImage = DebouncedValue<List<Mesh>, Widget> { meshes ->
	// like the above program, assume first attribute is position
	val minX = meshes.minOf { mesh ->
		mesh.vertices.minOf { vertex ->
			vertex[0].x
		}
	}
	val maxX = meshes.maxOf { mesh ->
		mesh.vertices.maxOf { vertex ->
			vertex[0].x
		}
	}
	val minY = meshes.minOf { mesh ->
		mesh.vertices.minOf { vertex ->
			vertex[0].y
		}
	}
	val maxY = meshes.maxOf { mesh ->
		mesh.vertices.maxOf { vertex ->
			vertex[0].y
		}
	}

	// scale the minimum amount to fit all dimensions to avoid stretching
	val scale = 2 / max(maxX - minX, maxY - minY)

	// scale to fit the viewport
	program.uniforms[0] = transform.apply {
		reset()
		scale(Vector3.of(scale, scale))
	}

	// draw to texture
	renderBackend.with(texture) {
		renderBackend.viewport(0, 0, RESOLUTION, RESOLUTION)
		renderBackend.clear()

		renderBackend.draw(program, meshes)
	}

	val textureId = renderBackend.getTexture(texture)

	Widget {
		image(textureId.toLong(), IMAGE_WIDTH.toFloat(), IMAGE_HEIGHT.toFloat())
	}
}

class ModelComponentWidgetFactory : WidgetFactory<Component> {
	override val type: Class<Component> = Component::class.java

	override fun get(t: Component): Widget? = WidgetFactory.get<Model>(t) {
		Widget {
			header("program", ImGuiTreeNodeFlags.DefaultOpen) {
				table("##shaders", 3, flags = ImGuiTableFlags.SizingFixedFit) {
					configColumn("Type")
					configColumn("Value")
					configColumn("Actions")
					headersRow()

					val shaders = program.shaders.toMutableList()

					if (shaders.isNotEmpty()) {
						var toRemove: Int? = null

						shaders.forEachIndexed { i, shader ->
							column { text(shader.type.name) }
							column {
								val source = shader.source
								text(
									when (source) {
										is Shader.Source.Path -> source.path
										else -> source.value
									}
								)
								tooltip(shader.source.value)
							}
							column {
								button("-##$i") { toRemove = i }
								tooltip("remove")
							}
						}

						// augment elements only after done iterating
						toRemove?.let {
							shaders.removeAt(it)
							program.shaders = shaders
						}
					}

					// to add new values
					var newType: Shader.Type? = null
					var newPath: String? = null
					column {
						input("##type", newType) {
							newType = it
							FileAccess.pickFile(FileAccess.Filter("GLSL Shader", "glsl", "vert", "frag"))?.let { newPath = it }
						}
						tooltip("create new")
					}

					newType?.let { type ->
						newPath?.let { path ->
							shaders += Shader(type, Shader.Source.Path(path))
							program.shaders = shaders
						}
					}
				}
			}

			header("meshes (${meshes.size})", ImGuiTreeNodeFlags.DefaultOpen) {
				if (meshes.isNotEmpty()) {
					tooltip {
						meshImage.set(meshes)()
					}
					if (meshes.size > 1) {
						tooltip {

						}

						list("##meshes") {
							meshes.forEachIndexed { i, mesh ->
								text(i)
								tooltip {
									meshImage.set(listOf(mesh))()
								}
							}
						}
					}
				}
			}
		}
	}

	override fun get(c: Class<out Component>, onNew: (Component) -> Unit): Widget? = WidgetFactory.get<Model>(c, onNew) {
		Widget {
			it(Model(Program()))
		}
	}
}

