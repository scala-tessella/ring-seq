import org.scalatest.*
import org.scalatest.flatspec.*
import org.scalatest.matchers.*

class RingVectorSpec extends RingVector with AnyFlatSpec with should.Matchers {

  val v = Vector(1, 2, 3, 4, 5)

  "A RingVector" can "be rotated one step to the right" in {
    v.rotateRight(1) shouldBe Vector(5, 1, 2, 3, 4)
  }

  it can "be rotated one step to the left, now starts where index 1 was" in {
    v.startAt(1) shouldBe Vector(2, 3, 4, 5, 1)
  }

  it can "be reflected" in {
    v.reflectAt() shouldBe Vector(1, 5, 4, 3, 2)
  }

}
