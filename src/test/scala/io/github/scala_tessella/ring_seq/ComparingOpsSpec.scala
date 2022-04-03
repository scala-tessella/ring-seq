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

}
