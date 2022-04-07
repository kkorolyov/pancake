package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.flub.data.Graph;
import dev.kkorolyov.pancake.platform.entity.Component;
import dev.kkorolyov.pancake.platform.math.Vector2;
import dev.kkorolyov.pancake.platform.math.Vector3;
import dev.kkorolyov.pancake.platform.math.Vectors;

import java.util.Arrays;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A closed boundary.
 * Boundaries can be one of:
 * {@code Round} - a single value representing the radius of a sphere
 * {@code Poly} - an undirected graph of vectors from a common origin of {@code (0, 0, 0)} representing vertices of a polyhedron
 */
public final class Bounds implements Component {
	private final Vector3[] vertices;
	// TODO edges maybe
	private final Vector2[] normals;
	private final double magnitude;

	private boolean correctable;

	public static Bounds box(Vector3 dimensions) {
		double halfX = dimensions.getX() / 2, halfY = dimensions.getY() / 2, halfZ = dimensions.getZ() / 2;

		return new Bounds(
				new Graph<Vector3, Void>()
						.putUndirected(
								Vectors.create(halfX, halfY, halfZ),
								Vectors.create(-halfX, halfY, halfZ),
								Vectors.create(halfX, -halfY, halfZ),
								Vectors.create(halfX, halfY, -halfZ)
						)
						.putUndirected(
								Vectors.create(-halfX, -halfY, -halfZ),
								Vectors.create(halfX, -halfY, -halfZ),
								Vectors.create(-halfX, halfY, -halfZ),
								Vectors.create(-halfX, -halfY, halfZ)
						)
						.putUndirected(
								Vectors.create(halfX, halfY, -halfZ),
								Vectors.create(-halfX, halfY, -halfZ),
								Vectors.create(halfX, -halfY, -halfZ)
						)
						.putUndirected(
								Vectors.create(halfX, -halfY, halfZ),
								Vectors.create(halfX, -halfY, -halfZ),
								Vectors.create(-halfX, -halfY, halfZ)
						)
						.putUndirected(
								Vectors.create(-halfX, halfY, halfZ),
								Vectors.create(-halfX, halfY, -halfZ),
								Vectors.create(-halfX, -halfY, halfZ)
						)
		);
	}
	public static Bounds round(double radius) {
		return new Bounds(new Graph<Vector3, Void>().put(Vectors.create(radius, 0, 0)));
	}

	/**
	 * Constructs new bounds defined by the given undirected vertex graph.
	 */
	public Bounds(Graph<Vector3, Void> vertices) {
		this.vertices = StreamSupport.stream(vertices.spliterator(), false)
				.map(Graph.Node::getValue)
				.distinct()
				.map(Vectors::create)
				.toArray(Vector3[]::new);
		normals = StreamSupport.stream(vertices.spliterator(), false)
				.flatMap(u -> u.getOutbounds().stream()
						.flatMap(v -> {
							Vector2 normal = Vectors.create((Vector2) v.getValue());
							normal.add(u.getValue(), -1);
							normal.orthogonal();
							normal.normalize();
							// add both normal and computed reverse
							return Stream.of(normal, Vectors.create(normal.getX() * -1, normal.getY() * -1));
						})
				)
				.distinct()
				.filter(n -> n.getX() != 0 || n.getY() != 0)
				.toArray(Vector2[]::new);
		magnitude = Arrays.stream(this.vertices)
				.mapToDouble(Vector3::magnitude)
				.max()
				.orElse(0);
	}

	/**
	 * Returns all the vertices in this boundary.
	 * WARNING: Any modifications to the returned array or its elements may contend with {@link #getNormals()} and {@link #getMagnitude()}.
	 */
	public Vector3[] getVertices() {
		return vertices;
	}

	/**
	 * Returns all the normal vectors to edges in this boundary.
	 * WARNING: Any modifications to the returned array or its elements may contend with {@link #getVertices()}.
	 */
	// TODO use ObservableVector to queue recomputation on vertex changes
	public Vector2[] getNormals() {
		return normals;
	}

	/**
	 * Returns the magnitude of the vertex vector furthest from the origin.
	 */
	public double getMagnitude() {
		return magnitude;
	}

	/**
	 * Returns {@code true} if this boundary has a single vertex - thus representing a sphere.
	 */
	public boolean isRound() {
		return vertices.length == 1;
	}

	/**
	 * Returns {@code true} if when this boundary intersects with another, the owning entity should be modified to alleviate the intersection.
	 */
	public boolean isCorrectable() {
		return correctable;
	}
	/**
	 * Sets correctable state to {@code correctable}.
	 * Defaults to {@code false}.
	 * @see #isCorrectable()
	 */
	public void setCorrectable(boolean correctable) {
		this.correctable = correctable;
	}
}
