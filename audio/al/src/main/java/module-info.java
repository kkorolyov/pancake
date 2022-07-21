module dev.kkorolyov.pancake.audio.al {
	requires java.desktop;
	requires kotlin.stdlib.jdk7;
	requires kotlin.stdlib.jdk8;
	requires org.slf4j;

	requires transitive org.lwjgl;
	requires transitive org.lwjgl.openal;

	requires transitive dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.core;

	exports dev.kkorolyov.pancake.audio.al;
	exports dev.kkorolyov.pancake.audio.al.component;
	exports dev.kkorolyov.pancake.audio.al.system;
}
