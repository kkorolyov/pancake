package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import static dev.kkorolyov.simplefuncs.stream.Iterables.append;

/**
 * Emits a collection of audio clips at a given configuration.
 * Contains both waiting and active audio.
 */
public class AudioEmitter implements Component {
	private final Queue<Media> waiting = new ArrayDeque<>();

	private final Collection<MediaPlayer> active = ConcurrentHashMap.newKeySet();

	/** @see #enqueue(Iterable) */
	public AudioEmitter enqueue(Media medium, Media... media) {
		return enqueue(append(medium, media));
	}
	/**
	 * @param media media to add to this emitter
	 * @return {@code this}
	 */
	public AudioEmitter enqueue(Iterable<Media> media) {
		media.forEach(waiting::add);
		return this;
	}

	/**
	 * Updates all currently active and waiting audio with given configuration.
	 * Activates all waiting audio.
	 * @param volume volume to emit audio at; constrained {@code [0.0, 1.0]}
	 * @param balance balance (panning) to emit audio at; constrained {@code [-1.0, 1.0]}
	 */
	public void apply(double volume, double balance) {
		// Update active media
		for (MediaPlayer mediaPlayer : active) {
			mediaPlayer.setVolume(volume);
			mediaPlayer.setBalance(balance);
		}

		// Activate waiting media
		for (Media media : waiting) {
			MediaPlayer mediaPlayer = new MediaPlayer(media);
			mediaPlayer.setVolume(volume);
			mediaPlayer.setBalance(balance);
			mediaPlayer.setOnEndOfMedia(() -> active.remove(mediaPlayer));
			mediaPlayer.play();

			active.add(mediaPlayer);
		}

		waiting.clear();
	}
}
