package io.github.scala_tessella.ring_seq.bench

import io.github.scala_tessella.ring_seq.RingSeq.*
import org.openjdk.jmh.annotations.*

import java.util.concurrent.TimeUnit

@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Benchmark)
class ComparingBench:

  @Param(Array("16", "256", "4096"))
  var size: Int = scala.compiletime.uninitialized

  var ring: Vector[Int]      = scala.compiletime.uninitialized
  var rotated: Vector[Int]   = scala.compiletime.uninitialized
  var unrelated: Vector[Int] = scala.compiletime.uninitialized
  var reversed: Vector[Int]  = scala.compiletime.uninitialized

  @Setup
  def setup(): Unit =
    ring = Vector.tabulate(size)(identity)
    rotated = ring.rotateLeft(size / 2)   // True case found halfway through the rotation enumeration.
    unrelated = ring.updated(0, -1)       // False case: every rotation must be enumerated.
    reversed = ring.reverse               // Pessimistic case for minRotationalHammingDistance: no rotation matches.

  @Benchmark def isRotationOf_match: Boolean =
    ring.isRotationOf(rotated)

  @Benchmark def isRotationOf_nomatch: Boolean =
    ring.isRotationOf(unrelated)

  @Benchmark def isRotationOrReflectionOf_nomatch: Boolean =
    ring.isRotationOrReflectionOf(unrelated)

  // ---------------------------------------------------------------------------
  // minRotationalHammingDistance — O(n²) in time and allocation.
  //
  // The current implementation materialises all `n` rotations via `ring.rotations`
  // (each rotation is a new CC[A] of size n) and runs a length-n hamming count on
  // each. Scaling `size` 16 → 256 → 4096 should show a quadratic growth both in
  // average time and in allocated bytes (use `-prof gc` to see allocation rates).
  //
  //     sbt benchmarks/Jmh/run -prof gc -- ComparingBench.minRotationalHammingDistance
  //
  // Three scenarios, covering the min/mid/max regimes of the pruning heuristic:
  //   - _rotation : target is a rotation of the ring → min distance 0 (best case)
  //   - _oneOff   : target differs by 1 element      → min distance 1 (mid)
  //   - _reversed : target is reverse(ring)          → min distance ~n (worst)
  // ---------------------------------------------------------------------------

  @Benchmark def minRotationalHammingDistance_rotation: Int =
    ring.minRotationalHammingDistance(rotated)

  @Benchmark def minRotationalHammingDistance_oneOff: Int =
    ring.minRotationalHammingDistance(unrelated)

  @Benchmark def minRotationalHammingDistance_reversed: Int =
    ring.minRotationalHammingDistance(reversed)
