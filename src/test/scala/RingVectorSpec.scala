import org.scalatest.*
import org.scalatest.flatspec.*
import org.scalatest.matchers.*

class RingVectorSpec extends AnyFlatSpec with RingVector with should.Matchers {

  val v = Vector(1, 2, 3, 4, 5)

  "A RingVector" can "be rotated one step to the right" in {
    v.rotateRight(1) shouldBe Vector(5, 1, 2, 3, 4)
  }

  it can "be rotated one step to the left, starting at index 1" in {
    v.startAt(1) shouldBe Vector(2, 3, 4, 5, 1)
  }

}
