package ringseq

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import org.scalacheck.Test.check
import org.scalatest.flatspec._
import org.scalatest.matchers._
import ringseq.RingSeq._

import scala.collection.Seq

class OMethodsSpec extends AnyFlatSpec with should.Matchers {

  val s = Seq(1, 2, 3, 4, 5)

  "A Seq considered as a ring" must "always have an element indexed before another one" in {
    assertThrows[IndexOutOfBoundsException] {
      s.apply(-1)
    }
    s.applyO(-1) shouldBe 5
  }

  it must "always have an element indexed after another one" in {
    assertThrows[IndexOutOfBoundsException] {
      s.apply(5)
    }
    s.applyO(5) shouldBe 1
  }

  "An empty Seq" must "have no indexed elements" in {
    assertThrows[IndexOutOfBoundsException] {
      Seq.empty.apply(0)
    }
    assertThrows[ArithmeticException] {
      Seq.empty.applyO(0)
    }
  }

  "Any non empty Seq" must "return an element for any index" in {
    val elems = Set(1, 3, 5)
    val gen: Gen[(Seq[Int], IndexO)] =
      for {
        seq <- Gen.nonEmptyContainerOf[Seq, Int](Gen.oneOf(elems))
        i <- arbitrary[IndexO]
      } yield (seq, i)
    check(
      forAll(gen)({ case (seq, i) => elems.contains(seq.applyO(i)) })
    )(_)
  }

  "A Seq considered as a ring" can "contain a circular segment" in {
    def isOdd: Int => Boolean = _ % 2 == 1
    s.segmentLength(isOdd, 4) shouldEqual 1
    s.segmentLengthO(isOdd, 4) shouldEqual 2
  }

  it can "be sliced to a circular slice" in {
    val (from, to) = (-1, 6)
    s.slice(from, to) shouldBe Seq(1, 2, 3, 4, 5)
    s.sliceO(from, to) shouldBe Seq(5, 1, 2, 3, 4, 5, 1)
  }

  it can "NOT be sliced to an empty circular slice" in {
    val (from, to) = (3, 3)
    s.slice(from, to) shouldBe Seq.empty
    s.sliceO(from, to) shouldBe Seq.empty
  }

  it can "NOT be sliced to a negative circular slice" in {
    val (from, to) = (4, 3)
    s.slice(from, to) shouldBe Seq.empty
    s.sliceO(from, to) shouldBe Seq.empty
  }

  val circularSlice = Seq(5, 1)

  it can "contain a circular slice" in {
    s.containsSlice(circularSlice) shouldBe false
    s.containsSliceO(circularSlice) shouldBe true
  }

  it can "return the index of a contained circular slice" in {
    s.indexOfSlice(circularSlice) shouldBe -1
    s.indexOfSliceO(circularSlice) shouldBe 4
  }

  it can "return the last index of a contained circular slice" in {
    s.lastIndexOfSlice(circularSlice) shouldBe -1
    s.lastIndexOfSliceO(circularSlice) shouldBe 4
  }
  
  it can "return the last index of a contained circular slice before end" in {
    s.lastIndexOfSlice(circularSlice, 5) shouldBe -1
    s.lastIndexOfSliceO(circularSlice, 5) shouldBe 4
  }
  
  it can "be slided circularly by one step" in {
    s.sliding(2).toList shouldBe List(
      Seq(1, 2),
      Seq(2, 3),
      Seq(3, 4),
      Seq(4, 5)
    )
    s.slidingO(2).toList shouldBe List(
      Seq(1, 2),
      Seq(2, 3),
      Seq(3, 4),
      Seq(4, 5),
      Seq(5, 1)
    )
  }

  it can "be slided circularly by two steps" in {
    s.sliding(2, 2).toList shouldBe List(
      Seq(1, 2),
      Seq(3, 4),
      Seq(5)
    )
    s.slidingO(2, 2).toList shouldBe List(
      Seq(1, 2),
      Seq(3, 4),
      Seq(5, 1),
      Seq(2, 3),
      Seq(4, 5)
    )
  }

}
