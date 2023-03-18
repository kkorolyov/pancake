import dev.kkorolyov.pancake.editor.factory.ActionWidgetFactory;
import dev.kkorolyov.pancake.editor.factory.ComponentWidgetFactory;

module dev.kkorolyov.pancake.editor {
	requires kotlin.stdlib;

	requires org.slf4j;

	requires org.lwjgl;
	requires org.lwjgl.opengl;
	requires org.lwjgl.stb;
	requires org.lwjgl.glfw;

	requires imgui.binding;
	requires imgui.lwjgl3;

	requires transitive dev.kkorolyov.pancake.platform;
	requires transitive dev.kkorolyov.pancake.graphics;

	requires io.github.classgraph;

	exports dev.kkorolyov.pancake.editor;
	exports dev.kkorolyov.pancake.editor.factory;
	exports dev.kkorolyov.pancake.editor.widget;

	opens icons;

	uses ComponentWidgetFactory;
	uses ActionWidgetFactory;
}
