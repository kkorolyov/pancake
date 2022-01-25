module dev.kkorolyov.pancake.demo.wiggles {
	requires kotlin.stdlib;

	requires dev.kkorolyov.flub;

	requires javafx.base;
	requires tornadofx;

	requires org.slf4j;
	requires org.apache.logging.log4j;

	requires dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.core;
	requires dev.kkorolyov.pancake.graphics.jfx;
	requires dev.kkorolyov.pancake.audio.jfx;
	requires dev.kkorolyov.pancake.input.jfx;

	requires dev.kkorolyov.pancake.editor;
	requires dev.kkorolyov.pancake.editor.core;

	exports dev.kkorolyov.pancake.demo.wiggles to tornadofx;
}
