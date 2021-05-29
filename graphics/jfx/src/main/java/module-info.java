module dev.kkorolyov.pancake.graphics.jfx {
	requires kotlin.stdlib;

	requires javafx.graphics;

	requires dev.kkorolyov.pancake.platform;
	// TODO Remove this dependency
	requires dev.kkorolyov.pancake.core;

	exports dev.kkorolyov.pancake.graphics.jfx;
	exports dev.kkorolyov.pancake.graphics.jfx.renderable;
	exports dev.kkorolyov.pancake.graphics.jfx.system;
	exports dev.kkorolyov.pancake.graphics.jfx.component;
}
