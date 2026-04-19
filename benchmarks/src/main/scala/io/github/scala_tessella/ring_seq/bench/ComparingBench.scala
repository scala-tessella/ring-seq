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

  @Setup
  def setup(): Unit =
    ring = Vector.tabulate(size)(identity)
    rotated = ring.rotateLeft(size / 2) // True case found halfway through the rotation enumeration.
    unrelated = ring.updated(0, -1) // False case: every rotation must be enumerated.

  @Benchmark def isRotationOf_match: Boolean =
    ring.isRotationOf(rotated)

  @Benchmark def isRotationOf_nomatch: Boolean =
    ring.isRotationOf(unrelated)

  @Benchmark def isRotationOrReflectionOf_nomatch: Boolean =
    ring.isRotationOrReflectionOf(unrelated)
