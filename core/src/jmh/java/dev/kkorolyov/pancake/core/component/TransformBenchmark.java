package dev.kkorolyov.pancake.core.component;

import dev.kkorolyov.pancake.platform.math.Matrix4;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

@Fork(1)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 3, time = 1)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class TransformBenchmark {
	@Benchmark
	public Matrix4 getMatrixRoot(TransformState state) {
		return state.root.getMatrix();
	}

	@Benchmark
	public Matrix4 getMatrixNest1(TransformState state) {
		return state.nest1.getMatrix();
	}

	@Benchmark
	public Matrix4 getMatrixNest4(TransformState state) {
		return state.nest4.getMatrix();
	}

	@State(Scope.Thread)
	public static class TransformState {
		Transform root = new Transform();
		Transform nest1 = new Transform();
		Transform nest4 = new Transform();

		@Setup
		public void setup() {
			nest1.setParent(new Transform());

			nest4.setParent(new Transform());
			nest4.getParent().setParent(new Transform());
			nest4.getParent().getParent().setParent(new Transform());
		}
	}
}
