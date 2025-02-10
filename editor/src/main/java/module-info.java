module dev.kkorolyov.pancake.editor {
	requires kotlin.stdlib;

	requires org.slf4j;

	requires org.lwjgl;
	requires org.lwjgl.opengl;
	requires org.lwjgl.stb;
	requires org.lwjgl.glfw;
	requires org.lwjgl.nfd;

	requires imgui.binding;
	requires imgui.lwjgl3;

	requires dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.graphics;

	requires io.github.classgraph;

	exports dev.kkorolyov.pancake.editor;
	exports dev.kkorolyov.pancake.editor.data;
	exports dev.kkorolyov.pancake.editor.factory;
	exports dev.kkorolyov.pancake.editor.widget;

	opens icons;

	uses dev.kkorolyov.pancake.editor.factory.WidgetFactory;
}
