package io.github.scala_tessella.ring_seq

import org.scalatest.flatspec._
import org.scalatest.matchers._
import RingSeq._

import scala.collection.Seq

class ScaladocExampleSpec extends AnyFlatSpec with should.Matchers {

  "The examples in Scaladoc" must "be correct" in {
    val seq = Seq(0, 1, 2)

    seq.applyO(3) shouldBe 0
    seq.rotateRight(1) shouldBe Seq(2, 0, 1)
    seq.rotateLeft(1) shouldBe Seq(1, 2, 0)
    seq.startAt(1) shouldBe Seq(1, 2, 0)
    seq.reflectAt() shouldBe Seq(0, 2, 1)
    seq.segmentLengthO(_ % 2 == 0, 2) shouldBe 2
    seq.sliceO(-1, 4) shouldBe Seq(2, 0, 1, 2, 0)
    seq.containsSliceO(Seq(2, 0, 1, 2, 0)) shouldBe true
    seq.indexOfSliceO(Seq(2, 0, 1, 2, 0)) shouldBe 2
    Seq(0, 1, 2, 0, 1, 2).lastIndexOfSliceO(Seq(2, 0)) shouldBe 5
    seq.slidingO(2).toList shouldBe List(Seq(0, 1), Seq(1, 2), Seq(2, 0))
    seq.rotations.toList shouldBe List(seq, Seq(1, 2, 0), Seq(2, 0, 1))
    seq.reflections.toList shouldBe List(seq, Seq(0, 2, 1))
    seq.reversions.toList shouldBe List(seq, Seq(2, 1, 0))
    seq.rotationsAndReflections.toList shouldBe List(seq, Seq(1, 2, 0), Seq(2, 0, 1), Seq(0, 2, 1), Seq(2, 1, 0), Seq(1, 0, 2))
    seq.isRotationOf(Seq(1, 2, 0)) shouldBe true
    seq.isReflectionOf(Seq(0, 2, 1)) shouldBe true
    seq.isReversionOf(Seq(2, 1, 0)) shouldBe true
    seq.isRotationOrReflectionOf(Seq(2, 0, 1)) shouldBe true
    Seq(0, 1, 2, 0, 1, 2).rotationalSymmetry shouldBe 2
    Seq(2, 1, 2, 2, 1, 2, 2, 1, 2, 2, 1, 2).symmetryIndices shouldBe List(0, 3, 6, 9)
    Seq(2, 1, 2, 2, 1, 2, 2, 1, 2, 2, 1, 2).symmetry shouldBe 4
  }

}
