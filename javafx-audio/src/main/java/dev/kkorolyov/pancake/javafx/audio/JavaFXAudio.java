package dev.kkorolyov.pancake.javafx.audio;

import dev.kkorolyov.pancake.platform.media.audio.Audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * {@link Audio} implemented through JavaFX.
 */
public class JavaFXAudio implements Audio {
	private static final Map<State, BiConsumer<? super MediaPlayer, ? super Runnable>> STATE_HANDLERS = Map.of(
			State.PLAY, MediaPlayer::setOnPlaying,
			State.PAUSE, MediaPlayer::setOnPaused,
			State.STOP, MediaPlayer::setOnStopped,
			State.END, MediaPlayer::setOnEndOfMedia
	);
	private static final Map<State, Consumer<? super MediaPlayer>> STATE_SETTERS = Map.of(
			State.PLAY, MediaPlayer::play,
			State.PAUSE, MediaPlayer::pause,
			State.STOP, MediaPlayer::stop,
			State.END, MediaPlayer::dispose
	);

	private final MediaPlayer player;

	/**
	 * Constructs a new JavaFX-backed audio instance.
	 * @param media backing JavaFX media instance
	 */
	public JavaFXAudio(Media media) {
		player = new MediaPlayer(media);
	}

	@Override
	public void setVolume(double volume) {
		player.setVolume(volume);
	}
	@Override
	public void setBalance(double balance) {
		player.setBalance(balance);
	}

	@Override
	public void on(State state, Runnable handler) {
		STATE_HANDLERS.get(state).accept(player, handler);
	}
	@Override
	public void state(State state) {
		STATE_SETTERS.get(state).accept(player);
	}
}
