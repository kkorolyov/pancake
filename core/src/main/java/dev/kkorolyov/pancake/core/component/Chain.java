package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Vector3;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Maintains references to a single positional and multiple rotational anchors.
 * Moves towards the positional anchor when the distance between the anchor and the chained entity passes some threshold.
 * Faces towards the nearest rotational anchor.
 */
public final class Chain implements Component {
	private Vector3 positionAnchor;
	private double play;
	private final Set<Vector3> rotationAnchors = new HashSet<>();

	/**
	 * Constructs a new chain.
	 * @param positionAnchor chained positional anchor
	 * @param play maximum distance between {@code positionAnchor} and the chained entity
	 * @param rotationAnchors chained rotational anchors
	 */
	public Chain(Vector3 positionAnchor, double play, Vector3... rotationAnchors) {
		this(positionAnchor, play, Arrays.asList(rotationAnchors));
	}
	/**
	 * Constructs a new chain.
	 * @param positionAnchor chained positional anchor
	 * @param play maximum distance between {@code positionAnchor} and the chained entity
	 * @param rotationAnchors chained rotational anchors
	 */
	public Chain(Vector3 positionAnchor, double play, Iterable<Vector3> rotationAnchors) {
		setPositionAnchor(positionAnchor, play);
		rotationAnchors.forEach(this::addRotationAnchor);
	}

	/** @return positional anchor, or {@code null} if not set */
	public Vector3 getPositionAnchor() {
		return positionAnchor;
	}
	/** @param anchor new positional anchor */
	public void setPositionAnchor(Vector3 anchor) {
		setPositionAnchor(anchor, play);
	}

	/** @return maximum distance this chain's positional anchor may move without being followed */
	public double getPlay() {
		return play;
	}
	/** @param play new positional anchor play threshold */
	public void setPlay(double play) {
		setPositionAnchor(positionAnchor, play);
	}

	/**
	 * @param anchor new positional anchor
	 * @param play maximum distance {@code anchor} may move without this chain following
	 */
	public void setPositionAnchor(Vector3 anchor, double play) {
		this.positionAnchor = anchor;
		this.play = play;
	}

	/**
	 * Adds a rotational anchor.
	 * @param anchor rotational anchor
	 * @return {@code true} if this chain did not already contain {@code anchor} as a rotational anchor
	 */
	public boolean addRotationAnchor(Vector3 anchor) {
		return rotationAnchors.add(anchor);
	}
	/**
	 * Removes a rotational anchor.
	 * @param anchor removed rotational anchor
	 * @return {@code true} if {@code anchor} was bound to this chain
	 */
	public boolean removeRotationAnchor(Vector3 anchor) {
		return rotationAnchors.remove(anchor);
	}

	/** @return all rotational anchors */
	public Iterable<Vector3> getRotationAnchors() {
		return rotationAnchors;
	}
}
