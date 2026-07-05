package io.github.scala_tessella.ring_seq

import io.github.scala_tessella.ring_seq.RingSeq._
import org.scalatest.flatspec._
import org.scalatest.matchers._

import scala.collection.Seq
import scala.collection.mutable.ArrayBuffer

class RingViewSpec extends AnyFlatSpec with should.Matchers {

  "A RingView" must "be obtained from any Seq with the ring method" in {
    List(0, 1, 2).ring.toVector shouldBe Vector(0, 1, 2)
    Vector(0, 1, 2).ring.toVector shouldBe Vector(0, 1, 2)
  }

  it must "be obtained from a String" in {
    "RING".ring.rotateRight(1).iterator.mkString shouldBe "GRIN"
  }

  it must "wrap circular indexing in both directions" in {
    val v = Seq(0, 1, 2).ring
    v(3) shouldBe 0
    v(-1) shouldBe 2
    v.lift(3) shouldBe Some(0)
    v.indexFrom(-1) shouldBe 2
  }

  it must "throw on indexing an empty ring, but lift to None" in {
    assertThrows[ArithmeticException](Seq.empty[Int].ring(0))
    Seq.empty[Int].ring.lift(0) shouldBe None
  }

  it must "rotate and reflect as the eager operations do, in any combination" in {
    val s = Vector(0, 1, 2, 3)
    s.ring.rotateRight(1).toVector shouldBe s.rotateRight(1)
    s.ring.rotateLeft(1).toVector shouldBe s.rotateLeft(1)
    s.ring.startAt(2).toVector shouldBe s.startAt(2)
    s.ring.reflectAt().toVector shouldBe s.reflectAt()
    s.ring.reverse.toVector shouldBe s.reverse
    s.ring.rotateRight(1).reflectAt(1).rotateLeft(2).toVector shouldBe
      s.rotateRight(1).reflectAt(1).rotateLeft(2)
  }

  it must "be an involution under reflectAt at the same index" in {
    val v = Vector(0, 1, 2, 3, 4).ring.startAt(2)
    v.reflectAt(3).reflectAt(3) shouldBe v
  }

  it must "compare views by the elements they present" in {
    Vector(0, 1, 2).ring.rotateLeft(1) shouldBe Vector(1, 2, 0).ring
    Vector(0, 1, 2).ring.rotateLeft(1).hashCode shouldBe Vector(1, 2, 0).ring.hashCode
    Vector(0, 1, 2).ring should not be Vector(0, 2, 1).ring
  }

  it must "produce all rotations as views" in {
    Vector(0, 1, 2).ring.rotations.map(_.toVector).toList shouldBe
      List(Vector(0, 1, 2), Vector(1, 2, 0), Vector(2, 0, 1))
  }

  it must "reflect later mutations of a mutable underlying sequence" in {
    val buffer = ArrayBuffer(0, 1, 2)
    val view   = buffer.ring.rotateLeft(1)
    view.toVector shouldBe Vector(1, 2, 0)
    buffer(0) = 9
    view.toVector shouldBe Vector(1, 2, 9)
  }

  it must "materialize to any collection" in {
    Seq(0, 1, 2).ring.rotateLeft(1).to(List) shouldBe List(1, 2, 0)
    Seq(0, 1, 2).ring.toSeq shouldBe Seq(0, 1, 2)
  }

  it must "expose necklace and symmetry operations in view coordinates" in {
    Vector(2, 0, 1).ring.canonical.toVector shouldBe Vector(0, 1, 2)
    Vector(3, 1, 2).ring.bracelet.toVector shouldBe Vector(1, 2, 3)
    Vector(0, 1, 2, 1).ring.reflectionalSymmetryAxes shouldBe List((Vertex(0), Vertex(2)))
    Vector(0, 1, 0, 1).ring.rotationalSymmetry shouldBe 2
  }

  it must "render a readable toString" in {
    Vector(0, 1, 2).ring.rotateLeft(1).toString shouldBe "RingView(1, 2, 0)"
  }

  it must "behave on the empty ring" in {
    val e = Vector.empty[Int].ring
    e.rotateRight(3) shouldBe e
    e.rotations.toList shouldBe List(e)
    e.slice(0, 3).toList shouldBe Nil
    e.sliding(2).toList shouldBe Nil
    e.canonical shouldBe e
  }

}
