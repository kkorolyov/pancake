module dev.kkorolyov.pancake.input.glfw {
	requires kotlin.stdlib.jdk7;
	requires org.slf4j;

	requires transitive org.lwjgl;
	requires org.lwjgl.natives;
	requires transitive org.lwjgl.glfw;
	requires org.lwjgl.glfw.natives;

	requires transitive dev.kkorolyov.pancake.platform;
	requires transitive dev.kkorolyov.pancake.input.common;
	requires dev.kkorolyov.pancake.core;

	exports dev.kkorolyov.pancake.input.glfw;
	exports dev.kkorolyov.pancake.input.glfw.input;
	exports dev.kkorolyov.pancake.input.glfw.system;
}
