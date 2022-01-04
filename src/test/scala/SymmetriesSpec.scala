import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import org.scalacheck.Test.check
import org.scalatest.*
import org.scalatest.flatspec.*
import org.scalatest.matchers.*

class SymmetriesSpec extends AnyFlatSpec with RingVector with should.Matchers {

  val v = Vector(1, 2, 3, 1, 2, 3, 1, 2, 3)

  "A Vector considered as a ring" can "have n-fold rotational symmetry" in {
    v.rotationalSymmetry shouldBe 3
  }

}
