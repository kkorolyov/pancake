import dev.kkorolyov.pancake.debug.ComponentDataFactory;
import dev.kkorolyov.pancake.debug.core.TransformComponentDataFactory;

module dev.kkorolyov.pancake.debug.core {
	requires kotlin.stdlib;

	requires javafx.base;
	requires tornadofx;

	requires dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.core;
	requires dev.kkorolyov.pancake.debug;

	provides ComponentDataFactory with TransformComponentDataFactory;
}
