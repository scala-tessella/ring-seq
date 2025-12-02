package io.github.scala_tessella.ring_seq

import org.scalatest.flatspec._
import org.scalatest.matchers._
import RingSeq._

import scala.collection.Seq

class SlicingOpsSpec extends AnyFlatSpec with TestHelper with should.Matchers {

  "A circular sequence" can "contain a circular segment" in {
    def isOdd: Int => Boolean = _ % 2 == 1
    s12345.segmentLength(isOdd, 4) shouldEqual 1
    s12345.segmentLengthO(isOdd, 4) shouldEqual 2
  }

  it can "be sliced to a circular slice" in {
    val (from, to) = (-1, 6)
    s12345.slice(from, to) shouldBe Seq(1, 2, 3, 4, 5)
    s12345.sliceO(from, to) shouldBe Seq(5, 1, 2, 3, 4, 5, 1)
    e.sliceO(from, to)
  }

  it can "NOT be sliced to an empty circular slice" in {
    val (from, to) = (3, 3)
    s12345.slice(from, to) shouldBe e
    s12345.sliceO(from, to) shouldBe e
  }

  it can "NOT be sliced to a negative circular slice" in {
    val (from, to) = (4, 3)
    s12345.slice(from, to) shouldBe e
    s12345.sliceO(from, to) shouldBe e
  }

  val circularSlice: Seq[Int] = Seq(5, 1)

  it can "contain a circular slice" in {
    s12345.containsSlice(circularSlice) shouldBe false
    s12345.containsSliceO(circularSlice) shouldBe true
  }

  it can "return the index of a contained circular slice" in {
    s12345.indexOfSlice(circularSlice) shouldBe -1
    s12345.indexOfSliceO(circularSlice) shouldBe 4
  }

  it can "return the last index of a contained circular slice" in {
    s12345.lastIndexOfSlice(circularSlice) shouldBe -1
    s12345.lastIndexOfSliceO(circularSlice) shouldBe 4
  }

  it can "return the last index of a contained circular slice before end" in {
    s12345.lastIndexOfSlice(circularSlice, 5) shouldBe -1
    s12345.lastIndexOfSliceO(circularSlice, 5) shouldBe 4
  }

}
