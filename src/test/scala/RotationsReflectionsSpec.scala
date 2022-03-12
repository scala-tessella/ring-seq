import org.scalacheck.Prop.forAll
import org.scalacheck.Test.check
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.flatspec._
import org.scalatest.matchers._

import math.Ordering.Implicits.seqOrdering

class RotationsReflectionsSpec extends AnyFlatSpec with RingSeq with should.Matchers {

  val s = Seq(1, 2, 3, 4, 5)
  val oneLeft = Seq(2, 3, 4, 5, 1)

  "A Seq considered as a ring" can "be rotated one step to the right" in {
    s.rotateRight(1) shouldBe Seq(5, 1, 2, 3, 4)
  }

  it can "be rotated one step to the left" in {
    s.rotateLeft(1) shouldBe oneLeft
    s.rotateRight(-1) shouldBe oneLeft
  }

  it can "be rotated to start where index 1 is" in {
    s.startAt(1) shouldBe oneLeft
  }

  it can "be reflected" in {
    s.reflectAt() shouldBe Vector(1, 5, 4, 3, 2)
  }

  it can "be reflected at a given index" in {
    s.reflectAt(2) shouldBe Vector(3, 2, 1, 5, 4)
  }

  it can "be reflected as reversed" in {
    s.reflectAt(-1) shouldBe s.reverse
  }

  it can "iterate on all rotations" in {
    s.rotations.toList shouldBe List(
      s,
      oneLeft,
      Seq(3, 4, 5, 1, 2),
      Seq(4, 5, 1, 2, 3),
      Seq(5, 1, 2, 3, 4)
    )
  }

  it can "iterate on all reflections" in {
    s.reflections.toList shouldBe List(
      s,
      Seq(1, 5, 4, 3, 2)
    )
  }

  it can "iterate on all reversions" in {
    s.reversions.toList shouldBe List(
      s,
      s.reverse
    )
  }

  it can "iterate on all rotations and reflections" in {
    s.rotationsAndReflections.toList shouldBe List(
      s,
      oneLeft,
      Seq(3, 4, 5, 1, 2),
      Seq(4, 5, 1, 2, 3),
      Seq(5, 1, 2, 3, 4),
      Seq(1, 5, 4, 3, 2),
      Seq(5, 4, 3, 2, 1),
      Seq(4, 3, 2, 1, 5),
      Seq(3, 2, 1, 5, 4),
      Seq(2, 1, 5, 4, 3)
    )
  }

  it can "return the sorted minimum rotation" in {
    Seq(1, 2, 3, 4, 1).minRotation shouldBe Seq(1, 1, 2, 3, 4)
  }

  it can "be the rotation of another Vector" in {
    s.isRotationOf(Seq(3, 4, 5, 1, 2)) shouldBe true
    s.rotations.forall(s.isRotationOf) shouldBe true
  }

  it can "be the reflection of another Vector" in {
    s.isReflectionOf(Seq(1, 5, 4, 3, 2)) shouldBe true
  }

  it can "be the rotation or reflection of another Vector" in {
    s.isRotationOrReflectionOf(Seq(3, 2, 1, 5, 4)) shouldBe true
    s.rotationsAndReflections.forall(s.isRotationOrReflectionOf) shouldBe true
  }

  "A List considered as a ring" can "be rotated one step to the right" in {
    s.toList.rotateRight(1) shouldBe List(5, 1, 2, 3, 4)
  }

  "A Vector considered as a ring" can "be rotated one step to the right" in {
    s.toVector.rotateRight(1) shouldBe Vector(5, 1, 2, 3, 4)
  }

  "All rotations of a Seq" must "contain itself" in {
    check(
      forAll(arbitrary[Seq[Int]])(seq => seq.rotations.contains(seq))
    )(_)
  }

  "All rotations and reflections of a Vector" must "contain itself" in {
    check(
      forAll(arbitrary[Seq[Int]])(seq => seq.rotationsAndReflections.contains(seq))
    )(_)
  }

  "A Vector" must "always be the rotation of itself" in {
    check(
      forAll(arbitrary[Seq[Int]])(seq => seq.isRotationOf(seq))
    )(_)
  }

  it must "always be the rotation or reflection of itself" in {
    check(
      forAll(arbitrary[Seq[Int]])(seq => seq.isRotationOrReflectionOf(seq))
    )(_)
  }

}
