package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.media.audio.Audio;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import static dev.kkorolyov.flopple.collections.Iterables.append;
import static dev.kkorolyov.flopple.collections.Iterables.concat;
import static dev.kkorolyov.pancake.platform.media.audio.Audio.State.END;
import static dev.kkorolyov.pancake.platform.media.audio.Audio.State.PLAY;

/**
 * Emits a collection of audio clips at a given configuration.
 * Contains both waiting and active audio.
 */
public class AudioEmitter implements Component {
	private final Queue<Audio> waiting = new ArrayDeque<>();

	private final Collection<Audio> active = ConcurrentHashMap.newKeySet();

	/** @see #enqueue(Iterable) */
	public AudioEmitter enqueue(Audio audio, Audio... audios) {
		return enqueue(append(audio, audios));
	}
	/**
	 * @param audio audio to add to this emitter
	 * @return {@code this}
	 */
	public AudioEmitter enqueue(Iterable<Audio> audio) {
		audio.forEach(waiting::add);
		return this;
	}

	/**
	 * Updates all currently active and waiting audio with given configuration.
	 * Activates all waiting audio.
	 * @param volume volume to emit audio at; constrained {@code [0.0, 1.0]}
	 * @param balance balance (panning) to emit audio at; constrained {@code [-1.0, 1.0]}
	 */
	public void apply(double volume, double balance) {
		for (Audio audio : concat(active, waiting)) {
			audio.setVolume(volume);
			audio.setBalance(balance);
		}
		for (Audio audio = waiting.poll(); audio != null; audio = waiting.poll()) {
			active.add(audio);

			audio.on(END, active::remove);
			audio.state(PLAY);
		}
	}
}
