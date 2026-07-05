package io.github.scala_tessella.ring_seq

import io.github.scala_tessella.ring_seq.RingSeq._
import org.scalatest.flatspec._
import org.scalatest.matchers._

/** Exhaustive reference check over all small rings, in the spirit of ring-seq-rs `tests/algebra.rs`: every
  * ring of length 0 to 4 over the alphabet {0, 1, 2} and of length 5 to 6 over {0, 1} is compared against
  * naive reference implementations built only from `reverse`, concatenation and equality — no index
  * arithmetic shared with the library code.
  */
class ExhaustiveSmallRingSpec extends AnyFlatSpec with should.Matchers {

  private def allRings(n: Int, alphabet: Int): Iterator[Vector[Int]] =
    if (n == 0) Iterator(Vector.empty)
    else allRings(n - 1, alphabet).flatMap(v => (0 until alphabet).iterator.map(v :+ _))

  private val rings: List[Vector[Int]] =
    ((0 to 4).flatMap(allRings(_, 3)) ++ (5 to 6).flatMap(allRings(_, 2))).toList

  private def rotate(s: Vector[Int], k: Int): Vector[Int] =
    s.drop(k) ++ s.take(k)

  private def naiveRotations(s: Vector[Int]): IndexedSeq[Vector[Int]] =
    s.indices.map(rotate(s, _))

  private def naiveRotationalSymmetry(s: Vector[Int]): Int =
    if (s.size < 2) 1 else naiveRotations(s).count(_ == s)

  private def naiveSymmetryIndices(s: Vector[Int]): List[Int] =
    s.indices.filter(shift => rotate(s.reverse, shift) == s).toList

  /** Axis geometry derived independently, in "half-position" space: vertex `i` sits at half-position `2i`,
    * the midpoint of edge `(i, i+1)` at `2i + 1`. The ring is symmetric about the axis through half-positions
    * `a` and `a + n` (mod `2n`) iff the reflection `i -> (a - i) mod n` fixes it.
    */
  private def naiveAxes(s: Vector[Int]): Set[Set[AxisLocation]] = {
    val n                                    = s.size
    def location(halfPos: Int): AxisLocation = {
      val h = halfPos % (2 * n)
      if (h % 2 == 0) Vertex(h / 2) else Edge((h - 1) / 2, n)
    }
    (0 until n)
      .filter(a => s.indices.forall(i => s(i) == s(java.lang.Math.floorMod(a - i, n))))
      .map(a => Set(location(a), location(a + n)))
      .toSet
  }

  import Ordering.Implicits.seqOrdering

  private def naiveCanonical(s: Vector[Int]): Vector[Int] =
    if (s.isEmpty) s else naiveRotations(s).min

  private def naiveCanonicalIndex(s: Vector[Int]): Int =
    if (s.size <= 1) 0 else s.indices.find(k => rotate(s, k) == naiveCanonical(s)).get

  private def naiveBracelet(s: Vector[Int]): Vector[Int] =
    if (s.isEmpty) s else (naiveRotations(s) ++ naiveRotations(s.reverse)).min

  "The exhaustive ring enumeration" must "cover the expected number of rings" in {
    rings.size shouldBe (1 + 3 + 9 + 27 + 81 + 32 + 64)
  }

  "Every small ring" must "match the naive reference for rotationalSymmetry" in
    rings.foreach { s =>

      withClue(s"ring $s: ") {
        s.rotationalSymmetry shouldBe naiveRotationalSymmetry(s)
      }
    }

  it must "match the naive reference for symmetryIndices and symmetry" in
    rings.foreach { s =>

      withClue(s"ring $s: ") {
        s.symmetryIndices shouldBe naiveSymmetryIndices(s)
        s.symmetry shouldBe naiveSymmetryIndices(s).size
      }
    }

  it must "match the naive reference for reflectionalSymmetryAxes" in
    rings.foreach { s =>

      withClue(s"ring $s: ") {
        val axes = s.reflectionalSymmetryAxes
        axes.size shouldBe naiveSymmetryIndices(s).size
        axes.map { case (first, second) =>
          Set(first, second)
        }.toSet shouldBe naiveAxes(s)
      }
    }

  it must "match the naive reference for canonicalIndex, canonical and bracelet" in
    rings.foreach { s =>

      withClue(s"ring $s: ") {
        s.canonicalIndex shouldBe naiveCanonicalIndex(s)
        s.canonical shouldBe naiveCanonical(s)
        s.bracelet shouldBe naiveBracelet(s)
      }
    }

}
