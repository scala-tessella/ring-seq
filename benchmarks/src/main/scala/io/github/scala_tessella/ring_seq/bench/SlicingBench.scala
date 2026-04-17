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
class SlicingBench:

  @Param(Array("16", "256"))
  var size: Int = scala.compiletime.uninitialized

  // Output is `size * growth` elements ⇒ exposes the quadratic `++` fold for high growth.
  @Param(Array("1", "10", "100"))
  var growth: Int = scala.compiletime.uninitialized

  var ringVector: Vector[Int] = scala.compiletime.uninitialized
  var ringList: List[Int]     = scala.compiletime.uninitialized

  @Setup
  def setup(): Unit =
    ringVector = Vector.tabulate(size)(identity)
    ringList = ringVector.toList

  @Benchmark def sliceO_Vector: Vector[Int] =
    ringVector.sliceO(0, size * growth)

  @Benchmark def sliceO_List: List[Int] =
    ringList.sliceO(0, size * growth)
