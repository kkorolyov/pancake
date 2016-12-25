package dev.kkorolyov.pancake.entity;

/**
 * A point on a 2D plane with independent x and y velocities.
 */
public class DynamicPoint {
	private int x,
							y;
	private int xVelocity,
							yVelocity;
	
	/**
	 * Constructs a new dynamic point with x and y positions and velocities equal to 0.
	 * @see #DynamicPoint(int, int, int, int)
	 */
	public DynamicPoint() {
		this(0, 0, 0, 0);
	}
	/**
	 * Constructs a new dynamic point with x and y velocities equal to 0.
	 * @see #DynamicPoint(int, int, int, int)
	 */
	public DynamicPoint(int x, int y) {
		this(x, y, 0, 0);
	}
	/**
	 * Constructs a new dynamic point.
	 * @param x initial x position
	 * @param y initial y position
	 * @param xVelocity initial x velocity
	 * @param yVelocity initial y velocity
	 */
	public DynamicPoint(int x, int y, int xVelocity, int yVelocity) {
		this.x = x;
		this.y = y;
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
	}
	
	/**
	 * Updates this point's x/y position using its current x/y velocity.
	 */
	public void update() {
		x += xVelocity;
		y += yVelocity;
	}
	
	/**
	 * Sets x and y velocities to 0.
	 */
	public void stop() {
		setXVelocity(0);
		setYVelocity(0);
	}
	
	/** @return x position */
	public int getX() {
		return x;
	}
	/** @param x new x position */
	public void setX(int x) {
		this.x = x;
	}
	
	/** @return y position */
	public int getY() {
		return y;
	}
	/** @param y new y position */
	public void setY(int y) {
		this.y = y;
	}
	
	/** @return x velocity */
	public int getXVelocity() {
		return xVelocity;
	}
	/** @param xVelocity new x velocity */
	public void setXVelocity(int xVelocity) {
		this.xVelocity = xVelocity;
	}
	
	/** @return y velocity */
	public int getYVelocity() {
		return yVelocity;
	}
	/** @param yVelocity new y velocity */
	public void setYVelocity(int yVelocity) {
		this.yVelocity = yVelocity;
	}
	
	/** @return this point's current position and velocity in the format: {@code (x, y) [xVelocity, yVelocity]} */
	@Override
	public String toString() {
		return "(" + x + ", " + y + ") [" + xVelocity + ", " + yVelocity + "]";
	}
}
