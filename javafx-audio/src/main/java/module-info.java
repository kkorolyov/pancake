module dev.kkorolyov.pancake.javafx.audio {
	requires kotlin.stdlib;

	requires javafx.media;

	requires dev.kkorolyov.flopple;

	requires dev.kkorolyov.pancake.platform;

	provides dev.kkorolyov.pancake.platform.media.audio.AudioFactory with dev.kkorolyov.pancake.javafx.audio.JavaFxAudioFactory;
}
