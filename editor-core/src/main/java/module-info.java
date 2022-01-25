import dev.kkorolyov.pancake.editor.ComponentDataFactory;
import dev.kkorolyov.pancake.editor.core.TransformComponentDataFactory;

module dev.kkorolyov.pancake.editor.core {
	requires kotlin.stdlib;

	requires javafx.base;
	requires tornadofx;

	requires dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.core;
	requires dev.kkorolyov.pancake.editor;

	provides ComponentDataFactory with TransformComponentDataFactory;
}
