import org.scalatest.*
import org.scalatest.flatspec.*
import org.scalatest.matchers.*

class SymmetriesSpec extends AnyFlatSpec with RingVector with should.Matchers {

  val v = Vector(1, 2, 3, 4, 5)
  val spin3 = Vector(1, 2, 3, 1, 2, 3, 1, 2, 3)
  val eptagon = Vector(6, 6, 6, 6, 6, 6, 6)
  val squaroid = Vector(2, 1, 2, 2, 1, 2, 2, 1, 2, 2, 1, 2)
  val axisOnElement = Vector(1, 2, 3, 4, 3, 2)
  val axisOffElement = Vector(1, 2, 3, 4, 4, 3, 2, 1)
  val axisOnOffElement = Vector(1, 2, 3, 4, 4, 3, 2)

  "A Vector considered as a ring" can "have n-fold rotational symmetry" in {
    v.rotationalSymmetry shouldBe 1
    spin3.rotationalSymmetry shouldBe 3
    eptagon.rotationalSymmetry shouldBe 7
    squaroid.rotationalSymmetry shouldBe 4
    axisOnElement.rotationalSymmetry shouldBe 1
    axisOffElement.rotationalSymmetry shouldBe 1
    axisOnOffElement.rotationalSymmetry shouldBe 1
  }

  it can "have axes of reflectional symmetry" in {
    v.symmetry shouldBe 0
    spin3.symmetry shouldBe 0
    eptagon.symmetry shouldBe 7
    squaroid.symmetry shouldBe 4
    axisOnElement.symmetry shouldBe 1
    axisOffElement.symmetry shouldBe 1
    axisOnOffElement.symmetry shouldBe 1
  }

  it can "return the indices closer to the axes of reflectional symmetry" in {
    v.symmetryIndices shouldBe Nil
    spin3.symmetryIndices shouldBe Nil
    eptagon.symmetryIndices shouldBe List(0, 1, 2, 3, 4, 5, 6) 
    squaroid.symmetryIndices shouldBe List(1, 4, 7, 10)
    axisOnElement.symmetryIndices shouldBe List(0)
    axisOffElement.symmetryIndices shouldBe List(3)
    axisOnOffElement.symmetryIndices shouldBe List(0)
  }

}
