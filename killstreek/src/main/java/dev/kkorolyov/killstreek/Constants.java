package dev.kkorolyov.killstreek;

import dev.kkorolyov.pancake.platform.math.Vector;

public final class Constants {
	public static final float MAX_SPEED = 50;
	public static final float OBJECT_MASS = .1f;
	public static final float PLAYER_MASS = 10;
	public static final float OBJECT_DAMPING = .9f;
	public static final float PLAYER_DAMPING = .5f;

	public static final Vector BOX = new Vector(1, 1);
	public static final float RADIUS = BOX.getX() / 2;

	private Constants() {}
}
