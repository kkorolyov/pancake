import dev.kkorolyov.pancake.editor.ComponentDataFactory;

module dev.kkorolyov.pancake.editor {
	requires kotlin.stdlib;

	requires tornadofx;
	requires javafx.base;
	requires javafx.graphics;

	requires org.slf4j;

	requires dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.core;

	exports dev.kkorolyov.pancake.editor;
	exports dev.kkorolyov.pancake.editor.controller to tornadofx;
	exports dev.kkorolyov.pancake.editor.view to tornadofx;

	opens icons;

	uses ComponentDataFactory;
}
