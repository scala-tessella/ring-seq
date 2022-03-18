package ringseq

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Prop.forAll
import org.scalacheck.Test.check
import org.scalatest.flatspec._
import org.scalatest.matchers._
import ringseq.RingSeq._

import scala.collection.Seq
import scala.collection.immutable.Queue
import scala.collection.mutable.{Buffer, ListBuffer, StringBuilder, Queue => MutableQueue}

class RotationsReflectionsSpec extends AnyFlatSpec with should.Matchers {

  val s = Seq(1, 2, 3, 4, 5)
  val oneLeft = Seq(2, 3, 4, 5, 1)

  "Any immutable Seq subtype" can "be rotated" in {
    "SCALA".rotateRight(2).mkString shouldEqual "LASCA"
    s.toList.rotateRight(2) shouldBe List(4, 5, 1, 2, 3)
    s.toVector.rotateRight(2) shouldBe Vector(4, 5, 1, 2, 3)
    Queue(1, 2, 3, 4, 5).rotateRight(2) shouldBe Queue(4, 5, 1, 2, 3)
  }

  "Any mutable Seq subtype" can "be rotated" in {
    s.toBuffer.rotateRight(2) shouldBe Buffer(4, 5, 1, 2, 3)
    ListBuffer(1, 2, 3, 4, 5).rotateRight(2) shouldBe ListBuffer(4, 5, 1, 2, 3)
    val sb = new StringBuilder("abcde")
    sb.rotateRight(2).toList shouldBe List('d', 'e', 'a', 'b', 'c')
    MutableQueue(1, 2, 3, 4, 5).rotateRight(2) shouldBe MutableQueue(4, 5, 1, 2, 3)
  }

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
    s.reflectAt() shouldBe Seq(1, 5, 4, 3, 2)
  }

  it can "be reflected at a given index" in {
    s.reflectAt(2) shouldBe Seq(3, 2, 1, 5, 4)
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

  it can "be the rotation of another Seq" in {
    s.isRotationOf(Seq(3, 4, 5, 1, 2)) shouldBe true
    s.rotations.forall(s.isRotationOf) shouldBe true
  }

  it can "be the reflection of another Seq" in {
    s.isReflectionOf(Seq(1, 5, 4, 3, 2)) shouldBe true
  }

  it can "be the reversion of another Seq" in {
    s.isReversionOf(Seq(5, 4, 3, 2, 1)) shouldBe true
  }

  it can "be the rotation or reflection of another Seq" in {
    s.isRotationOrReflectionOf(Seq(3, 2, 1, 5, 4)) shouldBe true
    s.rotationsAndReflections.forall(s.isRotationOrReflectionOf) shouldBe true
  }

  "All rotations of a Seq" must "contain itself" in {
    check(
      forAll(arbitrary[Seq[Int]])(seq => seq.rotations.contains(seq))
    )(_)
  }

  "All rotations and reflections of a Seq" must "contain itself" in {
    check(
      forAll(arbitrary[Seq[Int]])(seq => seq.rotationsAndReflections.contains(seq))
    )(_)
  }

  "A Seq" must "always be the rotation of itself" in {
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
