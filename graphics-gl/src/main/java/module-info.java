module dev.kkorolyov.pancake.graphics.gl {
	requires kotlin.stdlib.jdk7;
	requires kotlin.stdlib.jdk8;
	requires org.slf4j;

	requires transitive org.lwjgl;
	requires transitive org.lwjgl.opengl;

	requires transitive dev.kkorolyov.pancake.platform;
	requires transitive dev.kkorolyov.pancake.graphics.common;
	requires dev.kkorolyov.pancake.core;

	exports dev.kkorolyov.pancake.graphics.gl.mesh;
	exports dev.kkorolyov.pancake.graphics.gl.shader;
	exports dev.kkorolyov.pancake.graphics.gl.component;
	exports dev.kkorolyov.pancake.graphics.gl.system;
}
