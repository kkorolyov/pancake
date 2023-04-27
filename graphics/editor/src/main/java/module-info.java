import dev.kkorolyov.pancake.editor.factory.WidgetFactory;
import dev.kkorolyov.pancake.graphics.editor.ModelComponentWidgetFactory;
import dev.kkorolyov.pancake.graphics.editor.factory.SnapshotFactory;

module dev.kkorolyov.pancake.graphics.editor {
	requires kotlin.stdlib;

	requires org.slf4j;

	requires imgui.binding;

	requires dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.graphics;
	requires dev.kkorolyov.pancake.editor;

	exports dev.kkorolyov.pancake.graphics.editor;
	exports dev.kkorolyov.pancake.graphics.editor.factory;

	uses SnapshotFactory;

	provides WidgetFactory with
			ModelComponentWidgetFactory;
}
