package dev.kkorolyov.pancake.platform.math;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

@Fork(1)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 3, time = 1)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class MatrixBenchmark {
	@Benchmark
	public Matrix4 trs(TrsState state) {
		var result = state.matrix;
		result.translate(state.translation);
		result.rotate(state.rotRads, state.rotAxis);
		result.scale(state.scale);

		return result;
	}

	@State(Scope.Thread)
	public static class TrsState {
		Matrix4 matrix = Matrix4.identity();
		Vector3 translation = Vector3.of(1, 3, 5);
		Vector3 rotAxis = Vector3.of(0, 0, 1);
		double rotRads = Math.PI;
		Vector3 scale = Vector3.of(4, 6, 1);

		Matrix4 expected = Matrix4.of(
				0, 0, 0, 1,
				0, 0, 0, 3,
				0, 0, 0, 5,
				0, 0, 0, 1
		);

		@Setup(Level.Iteration)
		public void setup() {
			matrix.reset();
		}

		@TearDown(Level.Iteration)
		public void check() {
			assert expected.equals(matrix);
		}
	}
}
