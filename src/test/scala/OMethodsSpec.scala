import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import org.scalacheck.Test.check
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.flatspec._
import org.scalatest.matchers._

class OMethodsSpec extends AnyFlatSpec with RingVector with should.Matchers {

  val v = Vector(1, 2, 3, 4, 5)

  "A Vector considered as a ring" must "always have an element indexed before another one" in {
    assertThrows[IndexOutOfBoundsException] {
      v.apply(-1)
    }
    v.applyO(-1) shouldBe 5
  }

  it must "always have an element indexed after another one" in {
    assertThrows[IndexOutOfBoundsException] {
      v.apply(5)
    }
    v.applyO(5) shouldBe 1
  }

  "An empty Vector" must "have no indexed elements" in {
    assertThrows[IndexOutOfBoundsException] {
      Vector.empty.apply(0)
    }
    assertThrows[ArithmeticException] {
      Vector.empty.applyO(0)
    }
  }

  "Any non empty Vector" must "return an element for any index" in {
    val elems = Set(1, 3, 5)
    val gen: Gen[(Vector[Int], IndexO)] =
      for {
        list <- Gen.nonEmptyContainerOf[List, Int](Gen.oneOf(elems))
        i <- arbitrary[IndexO]
      } yield (list.toVector, i)
    check(
      forAll(gen)({ case (vector, i) => elems.contains(vector.applyO(i)) })
    )(_)
  }

  "A Vector considered as a ring" can "contain a circular segment" in {
    def isOdd: Int => Boolean = _ % 2 == 1
    v.segmentLength(isOdd, 4) shouldEqual 1
    v.segmentLengthO(isOdd, 4) shouldEqual 2
  }

  it can "be sliced to a circular slice" in {
    val (from, to) = (-1, 6)
    v.slice(from, to) shouldBe Vector(1, 2, 3, 4, 5)
    v.sliceO(from, to) shouldBe Vector(5, 1, 2, 3, 4, 5, 1)
  }

  it can "NOT be sliced to an empty circular slice" in {
    val (from, to) = (3, 3)
    v.slice(from, to) shouldBe Vector.empty
    v.sliceO(from, to) shouldBe Vector.empty
  }

  it can "NOT be sliced to a negative circular slice" in {
    val (from, to) = (4, 3)
    v.slice(from, to) shouldBe Vector.empty
    v.sliceO(from, to) shouldBe Vector.empty
  }

  val circularSlice = Vector(5, 1)

  it can "contain a circular slice" in {
    v.containsSlice(circularSlice) shouldBe false
    v.containsSliceO(circularSlice) shouldBe true
  }

  it can "return the index of a contained circular slice" in {
    v.indexOfSlice(circularSlice) shouldBe -1
    v.indexOfSliceO(circularSlice) shouldBe 4
  }

  it can "return the last index of a contained circular slice" in {
    v.lastIndexOfSlice(circularSlice) shouldBe -1
    v.lastIndexOfSliceO(circularSlice) shouldBe 4
  }
  
  it can "return the last index of a contained circular slice before end" in {
    v.lastIndexOfSlice(circularSlice, 5) shouldBe -1
    v.lastIndexOfSliceO(circularSlice, 5) shouldBe 4
  }
  
  it can "be slided circularly by one step" in {
    v.sliding(2).toList shouldBe List(
      Vector(1, 2),
      Vector(2, 3),
      Vector(3, 4),
      Vector(4, 5)
    )
    v.slidingO(2).toList shouldBe List(
      Vector(1, 2),
      Vector(2, 3),
      Vector(3, 4),
      Vector(4, 5),
      Vector(5, 1)
    )
  }

  it can "be slided circularly by two steps" in {
    v.sliding(2, 2).toList shouldBe List(
      Vector(1, 2),
      Vector(3, 4),
      Vector(5)
    )
    v.slidingO(2, 2).toList shouldBe List(
      Vector(1, 2),
      Vector(3, 4),
      Vector(5, 1),
      Vector(2, 3),
      Vector(4, 5)
    )
  }

}
