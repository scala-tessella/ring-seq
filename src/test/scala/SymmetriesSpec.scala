import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import org.scalacheck.Test.check
import org.scalatest.*
import org.scalatest.flatspec.*
import org.scalatest.matchers.*

class SymmetriesSpec extends AnyFlatSpec with RingVector with should.Matchers {

  val spin3 = Vector(1, 2, 3, 1, 2, 3, 1, 2, 3)
  val eptagon = Vector(6, 6, 6, 6, 6, 6, 6)
  val squaroid = Vector(2, 1, 2, 2, 1, 2, 2, 1, 2, 2, 1, 2)

  "A Vector considered as a ring" can "have n-fold rotational symmetry" in {
    spin3.rotationalSymmetry shouldBe 3
    eptagon.rotationalSymmetry shouldBe 7
    squaroid.rotationalSymmetry shouldBe 4
  }

  it can "have axes of reflectional symmetry" in {
    spin3.symmetry shouldBe 0
    eptagon.symmetry shouldBe 7
    squaroid.symmetry shouldBe 4
  }

  it can "return the indices closer to the axes of reflectional symmetry" in {
    spin3.symmetryIndices shouldBe Nil
    eptagon.symmetryIndices shouldBe List(0, 1, 2, 3, 4, 5, 6) 
    squaroid.symmetryIndices shouldBe List(1, 4, 7, 10) 
  }

}
