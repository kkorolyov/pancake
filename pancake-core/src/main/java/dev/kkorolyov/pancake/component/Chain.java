package dev.kkorolyov.pancake.component;

import dev.kkorolyov.pancake.entity.Component;

/**
 * "Anchors" to a transform at some offset position and moves when its anchor moves further than some "play" threshold.
 */
public class Chain implements Component {
	private final Transform anchor;
	private float play;

	/**
	 * Constructs a new chain.
	 * @param anchor transform anchored to
	 * @param play radius within which {@code anchor} may move without being followed by this chain
	 */
	public Chain(Transform anchor, float play) {
		this.anchor = anchor;
		this.play = play;
	}

	/** @return transform anchored to */
	public Transform getAnchor() {
		return anchor;
	}

	/** @return maximum radius within which anchor can move without this chain following */
	public float getPlay() {
		return play;
	}
}
