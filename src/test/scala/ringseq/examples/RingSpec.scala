package ringseq.examples

import org.scalatest.flatspec._
import org.scalatest.matchers._

class RingSpec extends AnyFlatSpec with should.Matchers {

  val underlying = List(1, 2, 3, 4)
  val ring = new Ring(underlying)

  "An instance of a examples.Ring class" must "be created with no rotation and no reversion" in {
    ring.currentHead shouldBe 1
    ring.headIndex shouldBe 0
    ring.isReversed shouldBe false
    ring.current shouldBe underlying
  }

  it can "be rotated by 1 step to the right" in {
    ring.rotateR()
    ring.headIndex shouldBe -1
    ring.isReversed shouldBe false
    ring.currentHead shouldBe 4
    ring.current shouldBe List(4, 1, 2, 3)
  }

  it can "be rotated back by 1 step to the left" in {
    ring.rotateL()
    ring.headIndex shouldBe 0
    ring.isReversed shouldBe false
    ring.currentHead shouldBe 1
    ring.current shouldBe underlying
  }

  it can "be reversed" in {
    ring.reverse()
    ring.headIndex shouldBe 0
    ring.isReversed shouldBe true
    ring.currentHead shouldBe 1
    ring.current shouldBe List(1, 4, 3, 2)
  }

  it can "be rotated while reversed by 1 step to the right" in {
    ring.rotateR(1)
    ring.headIndex shouldBe 1
    ring.isReversed shouldBe true
    ring.currentHead shouldBe 2
    ring.current shouldBe List(2, 1, 4, 3)
  }

  it can "be reversed again" in {
    ring.reverse()
    ring.headIndex shouldBe 1
    ring.isReversed shouldBe false
    ring.currentHead shouldBe 2
    ring.current shouldBe List(2, 3, 4, 1)
  }

  it can "be rotated back by 1 step to the right" in {
    ring.rotateR(1)
    ring.headIndex shouldBe 0
    ring.isReversed shouldBe false
    ring.currentHead shouldBe 1
    ring.current shouldBe underlying
  }

}
