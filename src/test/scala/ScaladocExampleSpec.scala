import RingSeq._

import org.scalatest.flatspec._
import org.scalatest.matchers._

class ScaladocExampleSpec extends AnyFlatSpec with should.Matchers {

  "The examples in Scaladoc" must "be correct" in {
    Seq(0, 1, 2).applyO(3) shouldBe 0
    Seq(0, 1, 2).rotateRight(1) shouldBe Seq(2, 0, 1)
    Seq(0, 1, 2).rotateLeft(1) shouldBe Seq(1, 2, 0)
    Seq(0, 1, 2).startAt(1) shouldBe Seq(1, 2, 0)
    Seq(0, 1, 2).reflectAt() shouldBe Seq(0, 2, 1)
    Seq(0, 1, 2).segmentLengthO(_ % 2 == 0, 2) shouldBe 2
    Seq(0, 1, 2).sliceO(-1, 4) shouldBe Seq(2, 0, 1, 2, 0)
    Seq(0, 1, 2).containsSliceO(Seq(2, 0, 1, 2, 0)) shouldBe true
    Seq(0, 1, 2).indexOfSliceO(Seq(2, 0, 1, 2, 0)) shouldBe 2
    Seq(0, 1, 2, 0, 1, 2).lastIndexOfSliceO(Seq(2, 0)) shouldBe 5
    Seq(0, 1, 2).slidingO(2).toList shouldBe List(Seq(0, 1), Seq(1, 2), Seq(2, 0))
    Seq(0, 1, 2).rotations.toList shouldBe List(Seq(0, 1, 2), Seq(1, 2, 0), Seq(2, 0, 1))
    Seq(0, 1, 2).reflections.toList shouldBe List(Seq(0, 1, 2), Seq(0, 2, 1))
    Seq(0, 1, 2).reversions.toList shouldBe List(Seq(0, 1, 2), Seq(2, 1, 0))
    Seq(0, 1, 2).rotationsAndReflections.toList shouldBe List(Seq(0, 1, 2), Seq(1, 2, 0), Seq(2, 0, 1), Seq(0, 2, 1), Seq(2, 1, 0), Seq(1, 0, 2))
    Seq(0, 1, 2).isRotationOf(Seq(1, 2, 0)) shouldBe true
    Seq(0, 1, 2).isReflectionOf(Seq(0, 2, 1)) shouldBe true
    Seq(0, 1, 2).isReversionOf(Seq(2, 1, 0)) shouldBe true
    Seq(0, 1, 2).isRotationOrReflectionOf(Seq(2, 0, 1)) shouldBe true
  }

}
