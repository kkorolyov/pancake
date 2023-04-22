import dev.kkorolyov.pancake.editor.factory.WidgetFactory;
import dev.kkorolyov.pancake.graphics.gl.editor.GLMeshWidgetFactory;

module dev.kkorolyov.pancake.graphics.gl.editor {
	requires kotlin.stdlib;

	requires org.slf4j;

	requires imgui.binding;

	requires dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.graphics;
	requires dev.kkorolyov.pancake.graphics.gl;
	requires dev.kkorolyov.pancake.editor;

	opens dev.kkorolyov.pancake.graphics.gl.editor.shaders;

	provides WidgetFactory with
			GLMeshWidgetFactory;
}
