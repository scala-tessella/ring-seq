package io.github.scala_tessella.ring_seq

import io.github.scala_tessella.ring_seq.RingSeq._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Prop.forAll
import org.scalacheck.Test.check
import org.scalatest.flatspec._
import org.scalatest.matchers._

import scala.collection.Seq

class NecklaceOpsSpec extends AnyFlatSpec with TestHelper with should.Matchers {

  "An empty circular sequence" can "have its canonical form" in {
    Seq.empty[Int].canonical shouldBe Seq.empty[Int]
    Seq.empty[Int].canonicalIndex shouldBe 0
  }

  "A single-element circular sequence" can "have its canonical form" in {
    Seq(7).canonical shouldBe Seq(7)
    Seq(7).canonicalIndex shouldBe 0
  }

  "A circular sequence" can "have its canonical (necklace) form computed" in {
    Seq(2, 0, 1).canonical shouldBe Seq(0, 1, 2)
    Seq(2, 0, 1).canonicalIndex shouldBe 1
  }

  it can "have its canonical form be itself when already smallest" in {
    s12345.canonical shouldBe s12345
    s12345.canonicalIndex shouldBe 0
  }

  "Canonical form" must "be invariant under rotation" in {
    check(
      forAll(arbitrary[Seq[Int]]) { seq =>

        seq.rotations.toList.forall(rot => rot.canonical == seq.canonical)
      }
    )(_)
  }

  "Two sequences" must "be rotations of each other iff their canonical forms are equal" in {
    check(
      forAll(arbitrary[Seq[Int]], arbitrary[Seq[Int]]) { (a, b) =>

        a.isRotationOf(b) == (a.size == b.size && a.canonical == b.canonical)
      }
    )(_)
  }

  "A circular sequence" can "have its bracelet (rotation+reflection) canonical form computed" in {
    // [3,1,2] rotates to [3,1,2],[1,2,3],[2,3,1]; reflections add [3,2,1],[2,1,3],[1,3,2].
    // Lex smallest of all 6 is [1,2,3].
    Seq(3, 1, 2).bracelet shouldBe Seq(1, 2, 3)
  }

  "Bracelet form" must "be invariant under rotation and reflection" in {
    check(
      forAll(arbitrary[Seq[Int]]) { seq =>

        seq.rotationsAndReflections.toList.forall(t => t.bracelet == seq.bracelet)
      }
    )(_)
  }

  it must "be lexicographically <= canonical" in {
    val ord = Ordering.Implicits.seqOrdering[List, Int]
    check(
      forAll(arbitrary[Seq[Int]]) { seq =>

        if (seq.isEmpty) true
        else ord.lteq(seq.bracelet.toList, seq.canonical.toList)
      }
    )(_)
  }

  "Canonical" can "be applied to a String" in {
    "NGRI".canonical.mkString shouldBe "GRIN"
  }

}
