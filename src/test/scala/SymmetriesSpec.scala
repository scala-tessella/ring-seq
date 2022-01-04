import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import org.scalacheck.Test.check
import org.scalatest.*
import org.scalatest.flatspec.*
import org.scalatest.matchers.*

class SymmetriesSpec extends AnyFlatSpec with RingVector with should.Matchers {

  val spin3 = Vector(1, 2, 3, 1, 2, 3, 1, 2, 3)
  val eptagon = Vector(6, 6, 6, 6, 6, 6, 6)

  "A Vector considered as a ring" can "have n-fold rotational symmetry" in {
    spin3.rotationalSymmetry shouldBe 3
    eptagon.rotationalSymmetry shouldBe 7
  }

  it can "have axes of reflectional symmetry" in {
    spin3.symmetry shouldBe 0
    eptagon.symmetry shouldBe 7 
  }

  it can "return the indices closer to the axes of reflectional symmetry" in {
    spin3.symmetryIndices shouldBe Nil
    eptagon.symmetry shouldBe List(0, 1, 2, 3, 4, 5, 6) 
  }

}
