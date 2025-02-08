import dev.kkorolyov.pancake.editor.factory.WidgetFactory;

module dev.kkorolyov.pancake.graphics.editor {
	requires kotlin.stdlib;

	requires org.slf4j;

	requires imgui.binding;

	requires dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.graphics;
	requires dev.kkorolyov.pancake.editor;

	exports dev.kkorolyov.pancake.graphics.editor;

	opens dev.kkorolyov.pancake.graphics.editor.shaders;

	provides WidgetFactory with
			dev.kkorolyov.pancake.graphics.editor.LensComponentWidgetFactory,
			dev.kkorolyov.pancake.graphics.editor.ModelComponentWidgetFactory;
}
