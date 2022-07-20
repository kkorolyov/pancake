import dev.kkorolyov.pancake.editor.ComponentWidgetFactory;

module dev.kkorolyov.pancake.editor {
	requires kotlin.stdlib.jdk7;
	requires kotlin.stdlib.jdk8;

	requires org.slf4j;

	requires org.lwjgl;
	requires org.lwjgl.opengl;
	requires org.lwjgl.stb;
	requires org.lwjgl.glfw;

	requires imgui.binding;
	requires imgui.lwjgl3;

	requires transitive dev.kkorolyov.pancake.platform;

	exports dev.kkorolyov.pancake.editor;
	exports dev.kkorolyov.pancake.editor.ext;
	exports dev.kkorolyov.pancake.editor.widget;

	opens icons;

	uses ComponentWidgetFactory;
}
