package io.github.scala_tessella.ring_seq

import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import org.scalacheck.Test.check
import org.scalatest.flatspec._
import org.scalatest.matchers._
import RingSeq._
import io.github.scala_tessella.ring_seq.SymmetryOps.{Edge, Vertex}

import scala.collection.Seq

class SymmetryOpsSpec extends AnyFlatSpec with TestHelper with should.Matchers {

  val spin3: Seq[Int]            = Seq(1, 2, 3, 1, 2, 3, 1, 2, 3)
  val eptagon: Seq[Int]          = Seq(6, 6, 6, 6, 6, 6, 6)
  val squaroid: Seq[Int]         = Seq(2, 1, 2, 2, 1, 2, 2, 1, 2, 2, 1, 2)
  val axisOnElement: Seq[Int]    = Seq(1, 2, 3, 4, 3, 2)
  val axisOffElement: Seq[Int]   = Seq(1, 2, 3, 4, 4, 3, 2, 1)
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
    squaroid.symmetryIndices shouldBe List(0, 3, 6, 9)
    axisOnElement.symmetryIndices shouldBe List(5)
    axisOffElement.symmetryIndices shouldBe List(0)
    axisOnOffElement.symmetryIndices shouldBe List(6)
  }

  "Any Seq" must "have rotational higher or equal than reflectional symmetry" in {
    val gen: Gen[Seq[Int]] = Gen.containerOf[Seq, Int](Gen.oneOf(1, 3, 5))
    check(
      forAll(gen)(seq => seq.rotationalSymmetry >= seq.symmetry)
    )(_)
  }

  behavior of "reflectionalSymmetryAxes"

  they should "be found for a triangle" in {
    Seq(1, 1, 1).reflectionalSymmetryAxes shouldBe
      List(
        (Vertex(1), Edge(2, 0)),
        (Vertex(2), Edge(0, 1)),
        (Vertex(0), Edge(1, 2))
      )
  }

  they should "be found for a doubled triangle" in {
    Seq(1, 2, 1, 2, 1, 2).reflectionalSymmetryAxes shouldBe
      List(
        (Vertex(2), Vertex(5)),
        (Vertex(1), Vertex(4)),
        (Vertex(0), Vertex(3))
      )
  }

  they should "be found for a square" in {
    Seq(1, 1, 1, 1).reflectionalSymmetryAxes shouldBe
      List(
        (Edge(1, 2), Edge(3, 0)),
        (Vertex(1), Vertex(3)),
        (Edge(0, 1), Edge(2, 3)),
        (Vertex(0), Vertex(2))
      )
  }

  they should "be found for a doubled square" in {
    Seq(1, 2, 1, 2, 1, 2, 1, 2).reflectionalSymmetryAxes shouldBe
      List(
        (Vertex(3), Vertex(7)),
        (Vertex(2), Vertex(6)),
        (Vertex(1), Vertex(5)),
        (Vertex(0), Vertex(4))
      )
  }

  they should "be found for a specular pentagon" in {
    Seq(1, 1, 2, 3, 2).reflectionalSymmetryAxes shouldBe
      List(
        (Vertex(3), Edge(0, 1))
      )
  }

}
