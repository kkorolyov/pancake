import dev.kkorolyov.pancake.platform.plugin.AudioFactory;

module dev.kkorolyov.pancake.plugin.audio.jfx {
	requires kotlin.stdlib;

	requires javafx.media;

	requires dev.kkorolyov.pancake.platform;

	provides AudioFactory with dev.kkorolyov.pancake.javafx.audio.JavaFxAudioFactory;
}
