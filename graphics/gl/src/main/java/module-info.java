import dev.kkorolyov.pancake.graphics.RenderBackend;

module dev.kkorolyov.pancake.graphics.gl {
	requires kotlin.stdlib;
	requires org.slf4j;

	requires transitive org.lwjgl;
	requires transitive org.lwjgl.opengl;
	requires transitive org.lwjgl.stb;

	requires dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.graphics;
	requires dev.kkorolyov.pancake.core;

	provides RenderBackend with dev.kkorolyov.pancake.graphics.gl.GLRenderBackend;
}
