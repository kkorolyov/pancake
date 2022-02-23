module dev.kkorolyov.pancake.graphics.gl {
	requires kotlin.stdlib.jdk7;
	requires org.slf4j;
	requires org.lwjgl.opengl;

	requires dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.core;
	requires transitive dev.kkorolyov.pancake.graphics.common;

	exports dev.kkorolyov.pancake.graphics.gl.mesh;
	exports dev.kkorolyov.pancake.graphics.gl.shader;
	exports dev.kkorolyov.pancake.graphics.gl.component;
	exports dev.kkorolyov.pancake.graphics.gl.system;
}
