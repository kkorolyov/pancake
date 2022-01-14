module dev.kkorolyov.pancake.audio.jfx {
	requires kotlin.stdlib;
	requires org.slf4j;

	requires javafx.media;

	requires dev.kkorolyov.flub;

	requires dev.kkorolyov.pancake.platform;
	// TODO Remove this dependency?
	requires dev.kkorolyov.pancake.core;

	exports dev.kkorolyov.pancake.audio.jfx;
	exports dev.kkorolyov.pancake.audio.jfx.component;
	exports dev.kkorolyov.pancake.audio.jfx.system;
}
