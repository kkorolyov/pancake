module dev.kkorolyov.pancake.graphics.jfx {
	requires kotlin.stdlib;
	requires org.slf4j;

	requires javafx.graphics;

	requires transitive dev.kkorolyov.pancake.platform;
	requires transitive dev.kkorolyov.pancake.graphics.common;
	// TODO Remove this dependency
	requires dev.kkorolyov.pancake.core;

	exports dev.kkorolyov.pancake.graphics.jfx.drawable;
	exports dev.kkorolyov.pancake.graphics.jfx.system;
	exports dev.kkorolyov.pancake.graphics.jfx.component;
}
