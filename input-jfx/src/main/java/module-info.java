module dev.kkorolyov.pancake.input.jfx {
	requires kotlin.stdlib;

	requires javafx.graphics;

	requires dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.core;

	exports dev.kkorolyov.pancake.input.jfx;
	exports dev.kkorolyov.pancake.input.jfx.component;
	exports dev.kkorolyov.pancake.input.jfx.system;
}
