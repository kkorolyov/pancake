package dev.kkorolyov.pancake.editor

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Level
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.Warmup
import java.util.concurrent.TimeUnit

@Fork(1)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 3, time = 1)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
open class ImBenchmark {
	@Benchmark
	fun threadLocalArray2(state: ImState): IntArray {
		val result = state.tArr2.get()
		return result
	}
	@Benchmark
	fun allocArray2(): IntArray {
		val result = IntArray(2)
		return result
	}

	@State(Scope.Thread)
	open class ImState {
		var tArr2 = ThreadLocal.withInitial { IntArray(2) }

		@Setup(Level.Iteration)
		fun setup() {
			tArr2 = ThreadLocal.withInitial { IntArray(2) }
		}
	}
}
