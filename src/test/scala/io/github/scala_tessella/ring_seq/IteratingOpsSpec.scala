package io.github.scala_tessella.ring_seq

import io.github.scala_tessella.ring_seq.RingSeq._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Prop.forAll
import org.scalacheck.Test.check
import org.scalatest.flatspec._
import org.scalatest.matchers._

import scala.collection.Seq

class IteratingOpsSpec extends AnyFlatSpec with TestHelper with should.Matchers {

  "A sequence" can "be slided by one step" in {
    "ABCDE".sliding(2).toList.map(_.mkString) shouldBe List(
      "AB",
      "BC",
      "CD",
      "DE"
    )
  }

  it can "be slided by two steps" in {
    "ABCDE".sliding(2, 2).toList.map(_.mkString) shouldBe List(
      "AB",
      "CD",
      "E"
    )
  }

  "The same sequence when considered circular" can "be slided circularly by one step" in {
    "ABCDE".slidingO(2).toList.map(_.mkString) shouldBe List(
      "AB",
      "BC",
      "CD",
      "DE",
      "EA"
    )
  }

  it can "be slided circularly by two steps" in {
    "ABCDE".slidingO(2, 2).toList.map(_.mkString) shouldBe List(
      "AB",
      "CD",
      "EA",
      "BC",
      "DE"
    )
  }

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

  "groupedO" can "partition a circular sequence into fixed-size blocks, wrapping the last one" in {
    "ABCDE".groupedO(2).toList.map(_.mkString) shouldBe List("AB", "CD", "EA")
  }

  it can "partition without wrap when size divides the ring" in {
    "ABCDEF".groupedO(2).toList.map(_.mkString) shouldBe List("AB", "CD", "EF")
  }

  it must "produce ceil(n/size) blocks, each of exactly size elements" in {
    val groups = "ABCDE".groupedO(3).toList
    groups.map(_.mkString) shouldBe List("ABC", "DEA")
    groups.forall(_.length == 3) shouldBe true
  }

  it can "produce a single block larger than the ring when size > n" in {
    "AB".groupedO(5).toList.map(_.mkString) shouldBe List("ABABA")
  }

  "An empty circular sequence" must "produce no groups" in {
    e.groupedO(2).toList shouldBe Nil
  }

  "zipWithIndexO" can "pair each element with its original index" in {
    Seq('a', 'b', 'c').zipWithIndexO(1).toList shouldBe List(
      ('b', 1),
      ('c', 2),
      ('a', 0)
    )
  }

  it must "default starting from index 0" in {
    Seq('a', 'b', 'c').zipWithIndexO().toList shouldBe List(
      ('a', 0),
      ('b', 1),
      ('c', 2)
    )
  }

  it must "yield no pairs for an empty sequence" in {
    e.zipWithIndexO().toList shouldBe Nil
  }

}
