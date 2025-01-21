module dev.kkorolyov.pancake.input.glfw {
	requires kotlin.stdlib;
	requires org.slf4j;

	requires transitive org.lwjgl;
	requires transitive org.lwjgl.glfw;

	requires dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.input;
	requires dev.kkorolyov.pancake.core;

	exports dev.kkorolyov.pancake.input.glfw.system;
}
