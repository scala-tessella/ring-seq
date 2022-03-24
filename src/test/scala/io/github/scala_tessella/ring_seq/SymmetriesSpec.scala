package io.github.scala_tessella.ring_seq

import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import org.scalacheck.Test.check
import org.scalatest.flatspec._
import org.scalatest.matchers._
import RingSeq._

import scala.collection.Seq

class SymmetriesSpec extends AnyFlatSpec with TestHelper with should.Matchers {

  val spin3: Seq[Int] = Seq(1, 2, 3, 1, 2, 3, 1, 2, 3)
  val eptagon: Seq[Int] = Seq(6, 6, 6, 6, 6, 6, 6)
  val squaroid: Seq[Int] = Seq(2, 1, 2, 2, 1, 2, 2, 1, 2, 2, 1, 2)
  val axisOnElement: Seq[Int] = Seq(1, 2, 3, 4, 3, 2)
  val axisOffElement: Seq[Int] = Seq(1, 2, 3, 4, 4, 3, 2, 1)
  val axisOnOffElement: Seq[Int] = Seq(1, 2, 3, 4, 4, 3, 2)

  "A Vector considered as a ring" can "have n-fold rotational symmetry" in {
    s12345.rotationalSymmetry shouldBe 1
    e.rotationalSymmetry shouldBe 1
    spin3.rotationalSymmetry shouldBe 3
    eptagon.rotationalSymmetry shouldBe 7
    squaroid.rotationalSymmetry shouldBe 4
    axisOnElement.rotationalSymmetry shouldBe 1
    axisOffElement.rotationalSymmetry shouldBe 1
    axisOnOffElement.rotationalSymmetry shouldBe 1
  }

  it can "have axes of reflectional symmetry" in {
    s12345.symmetry shouldBe 0
    e.symmetry shouldBe 0
    spin3.symmetry shouldBe 0
    eptagon.symmetry shouldBe 7
    squaroid.symmetry shouldBe 4
    axisOnElement.symmetry shouldBe 1
    axisOffElement.symmetry shouldBe 1
    axisOnOffElement.symmetry shouldBe 1
  }

  it can "return the indices closer to the axes of reflectional symmetry" in {
    s12345.symmetryIndices shouldBe Nil
    e.symmetryIndices shouldBe Nil
    spin3.symmetryIndices shouldBe Nil
    eptagon.symmetryIndices shouldBe List(0, 1, 2, 3, 4, 5, 6) 
    squaroid.symmetryIndices shouldBe List(1, 4, 7, 10)
    axisOnElement.symmetryIndices shouldBe List(0)
    axisOffElement.symmetryIndices shouldBe List(3)
    axisOnOffElement.symmetryIndices shouldBe List(0)
  }

  "Any Seq" must "have rotational higher or equal than reflectional symmetry" in {
    val gen: Gen[Seq[Int]] = Gen.containerOf[Seq, Int](Gen.oneOf(1, 3, 5))
    check(
      forAll(gen)(seq => seq.rotationalSymmetry >= seq.symmetry)
    )(_)
  }

}
