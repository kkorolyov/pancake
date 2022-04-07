module dev.kkorolyov.pancake.audio.al {
	requires java.desktop;
	requires kotlin.stdlib;
	requires kotlin.stdlib.jdk7;
	requires org.slf4j;
	requires org.lwjgl.openal;
	requires org.lwjgl.stb;

	requires transitive dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.core;

	exports dev.kkorolyov.pancake.audio.al;
	exports dev.kkorolyov.pancake.audio.al.component;
	exports dev.kkorolyov.pancake.audio.al.system;
}
