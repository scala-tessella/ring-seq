import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import org.scalacheck.Test.check
import org.scalatest.*
import org.scalatest.flatspec.*
import org.scalatest.matchers.*

class SymmetriesSpec extends AnyFlatSpec with RingVector with should.Matchers {

  val v = Vector(1, 2, 3, 1, 2, 3, 1, 2, 3)
  val eptagon = Vector(6, 6, 6, 6, 6, 6, 6)

  "A Vector considered as a ring" can "have n-fold rotational symmetry" in {
    v.rotationalSymmetry shouldBe 3
    eptagon.rotationalSymmetry shouldBe 3
  }

  it can "have axes of reflectional symmetry" in {
    v.symmetry shouldBe 0
    eptagon.symmetry shouldBe 7 
  }

}
