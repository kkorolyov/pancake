/*
 * Copyright (c) 2016-2019, Kirill Korolyov
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 *  Neither the name of Pancake nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package dev.kkorolyov.pancake.platform.media.graphic;

import dev.kkorolyov.pancake.platform.math.BoundedVector;
import dev.kkorolyov.pancake.platform.math.Vector;

/**
 * A constrained view on a renderable artifact.
 * Splits an overall artifact into a 3D set of partitions.
 * Partitioning constrained {@code [1, Integer#MAX_VALUE]} along each axis.
 */
public final class Viewport {
	private final Vector partitions;
	private final Vector current = new Vector();

	private final Vector origin = new Vector();
	private final Vector size = new Vector();

	private final Vector fullSize = new Vector();
	private final Vector lastFullSize = new Vector();

	/**
	 * Constructs a new 2D viewport.
	 * @see #Viewport(int, int, int)
	 */
	public Viewport(int xParts, int yParts) {
		this(xParts, yParts, 1);
	}
	/**
	 * Constructs a new viewport.
	 * @param xParts number of partitions along x-axis
	 * @param yParts number of partitions along y-axis
	 * @param zParts number of partitions along z-axis
	 */
	public Viewport(int xParts, int yParts, int zParts) {
		partitions = new BoundedVector(new Vector(xParts, yParts, zParts), Vector.all(1), Vector.all(Integer.MAX_VALUE));
	}

	/**
	 * Sets this viewport to {@code partition} index read left->right, top->bottom, nearest->furthest.
	 * @param partition index of partition to set to, constrained {@code [0, length()]}
	 * @return {@code this}
	 */
	public Viewport set(int partition) {
		partition = Math.max(0, Math.min(length(), partition));

		int parts2d = (int) (partitions.getX() * partitions.getY());
		int partXY = partition % parts2d;

		current.set(
				partXY % partitions.getX(),
				partXY / partitions.getX(),
				partition / parts2d
		);
		return this;
	}

	/** @return number of partitions in this viewport */
	public int length() {
		return (int) (partitions.getX() * partitions.getY() * partitions.getZ());
	}

	/** @return {@link #getOrigin(double, double, double)} for a 2D viewport */
	public Vector getOrigin(double width, double height) {
		return getOrigin(width, height, 1);
	}
	/**
	 * @param width width of artifact to apply this viewport to
	 * @param height height of artifact to apply this viewport to
	 * @param depth depth of artifact to apply this viewport to
	 * @return viewport origin calculated according to given {@code width}, {@code height}, {@code depth}
	 */
	public Vector getOrigin(double width, double height, double depth) {
		calculate(width, height, depth);
		return origin;
	}

	/** @return {@link #getSize(double, double, double)} for a 2D viewport */
	public Vector getSize(double width, double height) {
		return getSize(width, height, 1);
	}
	/**
	 * @param width width of artifact to apply this viewport to
	 * @param height height of artifact to apply this viewport to
	 * @param depth depth of artifact to apply this viewport to
	 * @return viewport size calculated according to given {@code width}, {@code height}, {@code depth}
	 */
	public Vector getSize(double width, double height, double depth) {
		calculate(width, height, depth);
		return size;
	}

	private void calculate(double width, double height, double depth) {
		if (!fullSize.set(width, height, depth).equals(lastFullSize)) {
			origin.set(current)
					.scale(
							size.set(fullSize)
									.invScale(partitions)
					);
		}
		lastFullSize.set(fullSize);
	}
}
