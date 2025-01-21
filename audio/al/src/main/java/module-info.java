module dev.kkorolyov.pancake.audio.al {
	requires java.desktop;
	requires kotlin.stdlib;
	requires org.slf4j;

	requires transitive org.lwjgl;
	requires transitive org.lwjgl.openal;

	requires dev.kkorolyov.pancake.platform;
	requires dev.kkorolyov.pancake.core;

	exports dev.kkorolyov.pancake.audio.al;
	exports dev.kkorolyov.pancake.audio.al.component;
	exports dev.kkorolyov.pancake.audio.al.system;
}
