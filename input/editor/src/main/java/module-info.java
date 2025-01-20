import dev.kkorolyov.pancake.editor.factory.WidgetFactory;

module dev.kkorolyov.pancake.input.editor {
	requires kotlin.stdlib;

	requires org.slf4j;

	requires org.lwjgl.glfw;
	requires imgui.binding;

	requires dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.input;
	requires dev.kkorolyov.pancake.editor;

	exports dev.kkorolyov.pancake.input.editor;

	provides WidgetFactory with
			dev.kkorolyov.pancake.input.editor.InputMapperComponentWidgetFactory;
}
