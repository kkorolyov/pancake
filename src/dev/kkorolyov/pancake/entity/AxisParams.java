package dev.kkorolyov.pancake.entity;

/**
 * An arbitrary number of parameters along an arbitrary number of axes.
 */
public abstract class AxisParams {
	protected final float[][] values;
	
	/**
	 * Constructs a new instance of this object.
	 * @param axes number of axes
	 * @param params number of parameters per axis
	 * @throws IllegalArgumentException if {@code axes} or {@code params} is {@code < 0}
	 */
	public AxisParams(int axes, int params) {
		if (axes < 0)
			throw new IllegalArgumentException("Axes must be >= 0: " + axes);
		if (params < 0)
			throw new IllegalArgumentException("Params must be >= 0: " + params);

		this.values = new float[axes][params];
	}
	
	protected void apply(int param, int[] values) {
		for (int i = 0; i < this.values.length; i++)
			this.values[i][param] = values[i];
	}
	
	/** @return number of axes encompassed by this object */
	public int axes() {
		return values.length;
	}
	
	protected float[] getParams(int param) {
		float[] params = new float[axes()];
		for (int i = 0; i < params.length; i++)
			params[i] = values[i][param];
		
		return params;
	}
	protected void setParams(int param, float value) {
		for (float[] axis : values)
			axis[param] = value;
	}
	
	protected float getValue(int axis, int parameter) {
		validateAxis(axis);
		return values[axis][parameter];
	}
	protected void setValue(int axis, int parameter, float value) {
		validateAxis(axis);
		values[axis][parameter] = value;
	}
	
	protected void validateAxis(int axis) {
		if (axis < 0)
			throw new IllegalArgumentException("Axis must be >= 0: " + axis);
		if (axis >= axes())
			throw new IllegalArgumentException("Axis must be < number of axes: " + axis);
	}
}
