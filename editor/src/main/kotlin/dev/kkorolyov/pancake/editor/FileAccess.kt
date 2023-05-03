package dev.kkorolyov.pancake.editor

import org.lwjgl.system.MemoryStack
import org.lwjgl.util.nfd.NFDFilterItem
import org.lwjgl.util.nfd.NativeFileDialog
import kotlin.io.path.Path
import kotlin.io.path.pathString
import kotlin.io.path.relativeTo

private val cwd = System.getProperty("user.dir")

/**
 * Provides for user selection of paths and files on the filesystem.
 */
object FileAccess {
	/**
	 * Opens a dialog to select a single file matching [filters].
	 * Returns a relative (from CWD) path to the selected file, if any chosen.
	 */
	fun pickFile(vararg filters: Filter): String? {
		MemoryStack.stackPush().use {
			val pathP = it.mallocPointer(1)

			val nfdFilters = NFDFilterItem.malloc(filters.size, it)
			filters.forEachIndexed { i, (name, spec) ->
				nfdFilters[i]
					.name(it.UTF8(name))
					.spec(it.UTF8(spec))
			}

			return when (NativeFileDialog.NFD_OpenDialog(pathP, nfdFilters, cwd)) {
				NativeFileDialog.NFD_OKAY -> {
					val path = Path(pathP.getStringUTF8(0)).relativeTo(Path(cwd)).pathString
					NativeFileDialog.NFD_FreePath(pathP.get(0))
					path
				}

				NativeFileDialog.NFD_ERROR -> {
					throw IllegalStateException(NativeFileDialog.NFD_GetError())
				}

				else -> null
			}
		}
	}

	/**
	 * Defines a single filter with descriptive [name] and `extensions` to match.
	 */
	class Filter(private val name: String, vararg extensions: String) {
		private val spec = extensions.joinToString(",")

		internal operator fun component1() = name
		internal operator fun component2() = spec
	}
}
