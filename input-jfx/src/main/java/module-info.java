module dev.kkorolyov.pancake.input.jfx {
	requires kotlin.stdlib;
	requires org.slf4j;

	requires javafx.graphics;

	requires transitive dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.core;
	requires transitive dev.kkorolyov.pancake.input.common;

	exports dev.kkorolyov.pancake.input.jfx;
	exports dev.kkorolyov.pancake.input.jfx.system;
}
