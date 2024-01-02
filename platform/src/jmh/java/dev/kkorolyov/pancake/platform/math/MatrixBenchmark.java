package dev.kkorolyov.pancake.platform.math;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

@Fork(1)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 3, time = 1)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class MatrixBenchmark {
	@Benchmark
	public Matrix4 trsReuse(TrsState state) {
		var result = state.matrix;
		result.reset();
		result.translate(state.translation);
		result.rotate(state.rotRads, state.rotAxis);
		result.scale(state.scale);

		return result;
	}
	@Benchmark
	public Matrix4 trsAlloc(TrsState state) {
		var result = Matrix4.identity();

		var translation = Matrix4.of(
				1, 0, 0, state.translation.getX(),
				0, 1, 0, state.translation.getY(),
				0, 0, 1, state.translation.getZ(),
				0, 0, 0, 1
		);

		double cosTheta = Math.cos(state.rotRads);
		double sinTheta = Math.sin(state.rotRads);
		double iCosTheta = 1 - cosTheta;
		var axis = state.rotAxis;

		var rotation = Matrix4.of(
				cosTheta + axis.getX() * axis.getX() * iCosTheta, axis.getX() * axis.getY() * iCosTheta - axis.getZ() * sinTheta, axis.getX() * axis.getZ() * iCosTheta + axis.getY() * sinTheta, 0,
				axis.getY() * axis.getX() * iCosTheta + axis.getZ() * sinTheta, cosTheta + axis.getY() * axis.getY() * iCosTheta, axis.getY() * axis.getZ() * iCosTheta - axis.getX() * sinTheta, 0,
				axis.getZ() * axis.getX() * iCosTheta - axis.getY() * sinTheta, axis.getZ() * axis.getY() * iCosTheta + axis.getX() * sinTheta, cosTheta + axis.getZ() * axis.getZ() * iCosTheta, 0,
				0, 0, 0, 1
		);

		var scale = Matrix4.of(
				state.scale.getX(), 0, 0, 0,
				0, state.scale.getY(), 0, 0,
				0, 0, state.scale.getZ(), 0,
				0, 0, 0, 1
		);

		result.multiply(translation);
		result.multiply(rotation);
		result.multiply(scale);

		return result;
	}

	@Benchmark
	public Matrix4 translateReuse(TrsState state) {
		var result = state.matrix;
		result.translate(state.translation);

		return result;
	}

	@Benchmark
	public Matrix4 alloc() {
		return Matrix4.identity();
	}
	@Benchmark
	public Matrix4 reset(TrsState state) {
		var result = state.matrix;
		result.reset();
		return result;
	}

	@State(Scope.Thread)
	public static class TrsState {
		Matrix4 matrix = Matrix4.identity();
		Vector3 translation = Vector3.of(1, 3, 5);
		Vector3 rotAxis = Vector3.of(0, 0, 1);
		double rotRads = Math.PI;
		Vector3 scale = Vector3.of(4, 6, 1);
	}
}
