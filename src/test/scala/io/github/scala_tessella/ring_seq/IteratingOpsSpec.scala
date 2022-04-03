package io.github.scala_tessella.ring_seq

import io.github.scala_tessella.ring_seq.RingSeq._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Prop.forAll
import org.scalacheck.Test.check
import org.scalatest.flatspec._
import org.scalatest.matchers._

import scala.collection.Seq

class IteratingOpsSpec extends AnyFlatSpec with TestHelper with should.Matchers {

  "An empty circular sequence" can "be iterated on all rotations" in {
    e.rotations.toList shouldBe List(e)
  }

  "A non-empty circular sequence" can "be iterated on all rotations" in {
    "ABCDE".rotations.toList.map(_.mkString) shouldBe List(
      "ABCDE",
      "BCDEA",
      "CDEAB",
      "DEABC",
      "EABCD"
    )
  }

  it can "iterate on all reflections" in {
    "ABCDE".reflections.toList.map(_.mkString) shouldBe List(
      "ABCDE",
      "AEDCB"
    )
  }

  it can "iterate on all reversions" in {
    "ABCDE".reversions.toList.map(_.mkString) shouldBe List(
      "ABCDE",
      "EDCBA"
    )
  }

  it can "iterate on all rotations and reflections" in {
    "ABCDE".rotationsAndReflections.toList.map(_.mkString) shouldBe List(
      "ABCDE",
      "BCDEA",
      "CDEAB",
      "DEABC",
      "EABCD",
      "AEDCB",
      "EDCBA",
      "DCBAE",
      "CBAED",
      "BAEDC"
    )
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

}
