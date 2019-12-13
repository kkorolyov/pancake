module dev.kkorolyov.pancake.audio.javafx {
	requires kotlin.stdlib;

	requires javafx.media;

	requires simple.funcs;

	requires dev.kkorolyov.pancake.platform;

	// Make services visible to Providers
	exports dev.kkorolyov.pancake.javafx.audio to simple.files;

	provides dev.kkorolyov.pancake.platform.media.audio.AudioFactory with dev.kkorolyov.pancake.javafx.audio.JavaFxAudioFactory;
}
