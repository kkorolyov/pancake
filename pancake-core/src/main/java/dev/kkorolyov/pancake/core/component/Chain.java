package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Vector;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Maintains references to a single positional and multiple rotational anchors.
 * Moves towards the positional anchor when the distance between the anchor and the chained entity passes some threshold.
 * Faces towards the nearest rotational anchor.
 */
public class Chain implements Component {
	private Vector positionAnchor;
	private float play;
	private final Set<Vector> rotationAnchors = new HashSet<>();

	/**
	 * Constructs a new chain.
	 * @param positionAnchor chained positional anchor
	 * @param play maximum distance between {@code positionAnchor} and the chained entity
	 * @param rotationAnchors chained rotational anchors
	 */
	public Chain(Vector positionAnchor, float play, Vector... rotationAnchors) {
		this(positionAnchor, play, Arrays.asList(rotationAnchors));
	}
	/**
	 * Constructs a new chain.
	 * @param positionAnchor chained positional anchor
	 * @param play maximum distance between {@code positionAnchor} and the chained entity
	 * @param rotationAnchors chained rotational anchors
	 */
	public Chain(Vector positionAnchor, float play, Iterable<Vector> rotationAnchors) {
		setPositionAnchor(positionAnchor, play);
		if (rotationAnchors != null) {
			for (Vector rotationAnchor : rotationAnchors) addRotationAnchor(rotationAnchor);
		}
	}

	/** @return positional anchor, or {@code null} if not set */
	public Vector getPositionAnchor() {
		return positionAnchor;
	}
	/** @param anchor new positional anchor */
	public void setPositionAnchor(Vector anchor) {
		setPositionAnchor(anchor, play);
	}

	/** @return maximum distance this chain's positional anchor may move without being followed */
	public float getPlay() {
		return play;
	}
	/** @param play new positional anchor play threshold */
	public void setPlay(float play) {
		setPositionAnchor(positionAnchor, play);
	}

	/**
	 * @param anchor new positional anchor
	 * @param play maximum distance {@code anchor} may move without this chain following
	 */
	public void setPositionAnchor(Vector anchor, float play) {
		this.positionAnchor = anchor;
		this.play = play;
	}

	/**
	 * Adds a rotational anchor.
	 * @param anchor rotational anchor
	 * @return {@code true} if this chain did not already contain {@code anchor} as a rotational anchor
	 */
	public boolean addRotationAnchor(Vector anchor) {
		return rotationAnchors.add(anchor);
	}
	/**
	 * Removes a rotational anchor.
	 * @param anchor removed rotational anchor
	 * @return {@code true} if {@code anchor} was bound to this chain
	 */
	public boolean removeRotationAnchor(Vector anchor) {
		return rotationAnchors.remove(anchor);
	}

	/** @return all rotational anchors */
	public Iterable<Vector> getRotationAnchors() {
		return rotationAnchors;
	}
}
