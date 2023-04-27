import dev.kkorolyov.pancake.editor.factory.WidgetFactory;
import dev.kkorolyov.pancake.graphics.editor.factory.SnapshotFactory;
import dev.kkorolyov.pancake.graphics.gl.editor.GLMeshWidgetFactory;
import dev.kkorolyov.pancake.graphics.gl.editor.GLSnapshotFactory;

module dev.kkorolyov.pancake.graphics.gl.editor {
	requires kotlin.stdlib;

	requires org.slf4j;

	requires imgui.binding;

	requires dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.graphics;
	requires dev.kkorolyov.pancake.graphics.gl;
	requires dev.kkorolyov.pancake.editor;
	requires dev.kkorolyov.pancake.graphics.editor;

	opens dev.kkorolyov.pancake.graphics.gl.editor.shaders;

	provides WidgetFactory with
			GLMeshWidgetFactory;
	provides SnapshotFactory with
			GLSnapshotFactory;
}
