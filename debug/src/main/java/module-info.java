import dev.kkorolyov.pancake.debug.ComponentDataFactory;

module dev.kkorolyov.pancake.debug {
	requires kotlin.stdlib;

	requires javafx.graphics;
	requires tornadofx;

	requires org.slf4j;

	requires dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.core;

	exports dev.kkorolyov.pancake.debug;
	exports dev.kkorolyov.pancake.debug.controller to tornadofx;
	exports dev.kkorolyov.pancake.debug.view to tornadofx;

	opens icons;

	uses ComponentDataFactory;
}
