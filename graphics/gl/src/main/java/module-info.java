module dev.kkorolyov.pancake.graphics.gl {
	requires kotlin.stdlib;
	requires org.slf4j;

	requires transitive org.lwjgl;
	requires transitive org.lwjgl.opengl;
	requires transitive org.lwjgl.stb;

	requires dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.graphics;
	requires dev.kkorolyov.pancake.core;

	exports dev.kkorolyov.pancake.graphics.gl;
	exports dev.kkorolyov.pancake.graphics.gl.resource;
	exports dev.kkorolyov.pancake.graphics.gl.system;
}
