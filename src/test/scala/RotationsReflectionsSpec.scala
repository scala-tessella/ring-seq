import org.scalatest.*
import org.scalatest.flatspec.*
import org.scalatest.matchers.*

import math.Ordering.Implicits.seqOrdering

class RotationsReflectionsSpec extends AnyFlatSpec with RingVector with should.Matchers {

  val v = Vector(1, 2, 3, 4, 5)
  val oneLeft = Vector(2, 3, 4, 5, 1)

  "A Vector considered as a ring" can "be rotated one step to the right" in {
    v.rotateRight(1) shouldBe Vector(5, 1, 2, 3, 4)
  }

  it can "be rotated one step to the left" in {
    v.rotateLeft(1) shouldBe oneLeft
    v.rotateRight(-1) shouldBe oneLeft
  }

  it can "be rotated to start where index 1 is" in {
    v.startAt(1) shouldBe oneLeft
  }

  it can "be reflected" in {
    v.reflectAt() shouldBe Vector(1, 5, 4, 3, 2)
  }

  it can "be reflected at a given index" in {
    v.reflectAt(2) shouldBe Vector(3, 2, 1, 5, 4)
  }

  it can "iterate on all rotations" in {
    v.allRotations.toList shouldBe List(
      v,
      oneLeft,
      Vector(3, 4, 5, 1, 2),
      Vector(4, 5, 1, 2, 3),
      Vector(5, 1, 2, 3, 4)
    )
  }

  it can "return the sorted minimum rotation" in {
    Vector(1, 2, 3, 4, 1).minRotation shouldBe Vector(1, 1, 2, 3, 4)
  }

  it can "be the rotation of another Vector" in {
    v.isRotationOf(Vector(3, 4, 5, 1, 2)) shouldBe true
  }

  it can "be the reflection of another Vector" in {
    v.isReflectionOf(Vector(1, 5, 4, 3, 2)) shouldBe true
  }

  it can "be the rotation or reflection of another Vector" in {
    v.isRotationOrReflectionOf(Vector(3, 2, 1, 5, 4)) shouldBe true
  }
}
