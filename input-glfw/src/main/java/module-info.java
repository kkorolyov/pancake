module dev.kkorolyov.pancake.input.glfw {
	requires kotlin.stdlib.jdk7;
	requires org.slf4j;

	requires transitive org.lwjgl.glfw;

	requires transitive dev.kkorolyov.pancake.platform;
	requires transitive dev.kkorolyov.pancake.input.common;
	requires dev.kkorolyov.pancake.core;

	exports dev.kkorolyov.pancake.input.glfw;
	exports dev.kkorolyov.pancake.input.glfw.input;
	exports dev.kkorolyov.pancake.input.glfw.system;
}
