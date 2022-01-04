import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import org.scalacheck.Test.check
import org.scalatest.*
import org.scalatest.flatspec.*
import org.scalatest.matchers.*

class AlternativeMethodsSpec extends AnyFlatSpec with RingVector with should.Matchers {

  val v = Vector(1, 2, 3, 4, 5)

  "A Vector considered as a ring" must "always have an element indexed before another one" in {
    assertThrows[IndexOutOfBoundsException] { // Result type: Assertion
      v.apply(-1)
    }
    v.applyO(-1) shouldBe 5
  }

  it must "always have an element indexed after another one" in {
    assertThrows[IndexOutOfBoundsException] { // Result type: Assertion
      v.apply(5)
    }
    v.applyO(5) shouldBe 1
  }

  "An empty Vector" must "have no indexed elements" in {
    assertThrows[IndexOutOfBoundsException] { // Result type: Assertion
      Vector.empty.apply(0)
    }
    assertThrows[IndexOutOfBoundsException] { // Result type: Assertion
      Vector.empty.applyO(0)
    }
  }

  it must "return an element for any index" in {
    check(forAll { (i: Int) => v.contains(v.applyO(i)) })
  }

  it can "be sliced to a circular slice" in {
    v.sliceO(-1, 6) shouldBe Vector(5, 1, 2, 3, 4, 5, 1)
  }

  it can "contain a circular slice" in {
    v.containsSliceO(Vector(5, 1)) shouldBe true
  }

  it can "return the index of a contained circular slice" in {
    v.indexOfSliceO(Vector(4, 5, 1)) shouldBe 3
  }
  
  it can "be ordinarily slided" in {
    v.sliding(2).toList shouldBe List(
      Vector(1, 2),
      Vector(2, 3),
      Vector(3, 4),
      Vector(4, 5)
    )
    v.sliding(2, 2).toList shouldBe List(
      Vector(1, 2),
      Vector(3, 4),
      Vector(5)
    )
  }

  it can "be slided to circular windows" in {
    v.slidingO(2).toList shouldBe List(
      Vector(1, 2),
      Vector(2, 3),
      Vector(3, 4),
      Vector(4, 5),
      Vector(5, 1)
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
