module dev.kkorolyov.pancake.audio.javafx {
	requires javafx.media;

	requires dev.kkorolyov.pancake.platform;

	provides dev.kkorolyov.pancake.platform.media.audio.AudioFactory with dev.kkorolyov.pancake.audio.javafx.JavaFXAudioFactory;
}
