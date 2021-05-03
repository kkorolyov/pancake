import dev.kkorolyov.pancake.platform.service.AudioFactory;

module dev.kkorolyov.pancake.javafx.audio {
	requires kotlin.stdlib;

	requires javafx.media;

	requires dev.kkorolyov.pancake.platform;

	provides AudioFactory with dev.kkorolyov.pancake.javafx.audio.JavaFxAudioFactory;
}
