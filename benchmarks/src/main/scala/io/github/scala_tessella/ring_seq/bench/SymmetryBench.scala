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
class SymmetryBench:

  @Param(Array("16", "256", "4096"))
  var size: Int = scala.compiletime.uninitialized

  // Period 4 ⇒ rotational symmetry of order size/4 (worst case: many full-length comparisons succeed).
  var symmetricVector: Vector[Int] = scala.compiletime.uninitialized
  var symmetricList: List[Int]     = scala.compiletime.uninitialized

  // No non-trivial symmetry: each candidate shift mismatches early.
  var asymmetricVector: Vector[Int] = scala.compiletime.uninitialized

  // A palindromic ring (max reflectional symmetry).
  var palindromicVector: Vector[Int] = scala.compiletime.uninitialized

  @Setup
  def setup(): Unit =
    symmetricVector = Vector.tabulate(size)(_ % 4)
    symmetricList = symmetricVector.toList
    asymmetricVector = Vector.tabulate(size)(identity)
    palindromicVector = Vector.tabulate(size)(i => Math.min(i, size - 1 - i))

  @Benchmark def rotationalSymmetry_Vector_symmetric: Int =
    symmetricVector.rotationalSymmetry

  @Benchmark def rotationalSymmetry_Vector_asymmetric: Int =
    asymmetricVector.rotationalSymmetry

  @Benchmark def rotationalSymmetry_List_symmetric: Int =
    symmetricList.rotationalSymmetry

  @Benchmark def symmetry_Vector_symmetric: Int =
    symmetricVector.symmetry

  @Benchmark def symmetry_Vector_palindromic: Int =
    palindromicVector.symmetry
