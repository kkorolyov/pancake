module dev.kkorolyov.pancake.audio.al {
	requires java.desktop;
	requires kotlin.stdlib.jdk7;
	requires org.slf4j;

	requires transitive org.lwjgl;
	requires org.lwjgl.natives;
	requires transitive org.lwjgl.openal;
	requires org.lwjgl.openal.natives;
	requires transitive org.lwjgl.stb;
	requires org.lwjgl.stb.natives;

	requires transitive dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.core;

	exports dev.kkorolyov.pancake.audio.al;
	exports dev.kkorolyov.pancake.audio.al.component;
	exports dev.kkorolyov.pancake.audio.al.system;
}
