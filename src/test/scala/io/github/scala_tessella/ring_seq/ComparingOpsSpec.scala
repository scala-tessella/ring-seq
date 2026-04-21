package io.github.scala_tessella.ring_seq

import io.github.scala_tessella.ring_seq.RingSeq._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Prop.forAll
import org.scalacheck.Test.check
import org.scalatest.flatspec._
import org.scalatest.matchers._

import scala.collection.Seq

class ComparingOpsSpec extends AnyFlatSpec with should.Matchers {

  "A circular sequence" can "be the rotation of another sequence" in {
    "ABCDE".isRotationOf("CDEAB") shouldBe true
    "ABCDE".rotations.forall("ABCDE".isRotationOf) shouldBe true
  }

  it can "be the reflection of another Seq" in {
    "ABCDE".isReflectionOf("AEDCB") shouldBe true
  }

  it can "be the reversion of another Seq" in {
    "ABCDE".isReversionOf("EDCBA") shouldBe true
  }

  it can "be the rotation or reflection of another Seq" in {
    "ABCDE".isRotationOrReflectionOf("CBAED") shouldBe true
    "ABCDE".rotationsAndReflections.forall("ABCDE".isRotationOrReflectionOf) shouldBe true
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

  "alignTo" can "find the rotation offset that produces a given sequence" in {
    Seq(0, 1, 2).alignTo(Seq(2, 0, 1)) shouldBe Some(2)
    Seq(0, 1, 2).alignTo(Seq(0, 1, 2)) shouldBe Some(0)
  }

  it must "return None when no rotation matches" in {
    Seq(0, 1, 2).alignTo(Seq(1, 0, 2)) shouldBe None
  }

  it must "return None when sizes differ" in {
    Seq(0, 1, 2).alignTo(Seq(0, 1)) shouldBe None
  }

  it must "return Some(0) for two empty sequences" in {
    Seq.empty[Int].alignTo(Seq.empty[Int]) shouldBe Some(0)
  }

  it must "agree with isRotationOf" in {
    check(
      forAll(arbitrary[Seq[Int]]) { seq =>

        seq.rotations.toList.zipWithIndex.forall { case (rot, _) =>
          val k = seq.alignTo(rot)
          k.isDefined && seq.startAt(k.get) == rot
        }
      }
    )(_)
  }

  "hammingDistance" can "count positional mismatches" in {
    Seq(1, 0, 1, 1).hammingDistance(Seq(1, 1, 0, 1)) shouldBe 2
    Seq(1, 2, 3).hammingDistance(Seq(1, 2, 3)) shouldBe 0
  }

  it must "throw when sizes differ" in {
    an[IllegalArgumentException] should be thrownBy Seq(1, 2).hammingDistance(Seq(1, 2, 3))
  }

  "minRotationalHammingDistance" must "be 0 when that is a rotation of this" in {
    Seq(1, 2, 3, 4).minRotationalHammingDistance(Seq(3, 4, 1, 2)) shouldBe 0
  }

  it must "find the rotation that minimizes mismatches" in {
    // Original [0,0,1,1,0]; rotated by 2: [1,1,0,0,0] vs target [1,1,0,0,1] — 1 mismatch.
    Seq(0, 0, 1, 1, 0).minRotationalHammingDistance(Seq(1, 1, 0, 0, 1)) shouldBe 1
  }

}
