package io.github.scala_tessella.ring_seq

import io.github.scala_tessella.ring_seq.IndexingOps.Index

import scala.collection.SeqOps

/** Provides symmetry operations for a `Seq` considered circular */
object SymmetryOps {

  /** A location on the circular sequence where a symmetry axis can pass through.
    *   - Vertex: The axis passes directly through the element at this index.
    *   - Edge: The axis passes between the elements at these indices.
    */
  sealed trait AxisLocation
  case class Vertex(i: Index)         extends AxisLocation
  case class Edge(i: Index, j: Index) extends AxisLocation

  /** Universal trait providing symmetry decorators for a `Seq` considered circular. */
  trait SymmetryDecorators[A, CC[B] <: SeqOps[B, CC, CC[B]]]
      extends Any
      with TransformingOps.TransformingDecorators[A, CC] {

    /** Computes the order of rotational symmetry possessed by this circular sequence, that is the number of
      * times the sequence matches itself as it makes a full rotation.
      *
      * @return
      *   An integer between 1 and the size of the sequence. 1 means no symmetry (only identity), max means a
      *   perfect circle (e.g., all elements equal).
      * @example
      *   {{{Seq(0, 1, 2, 0, 1, 2).rotationalSymmetry // 2}}}
      */
    def rotationalSymmetry: Int = {
      val n = ring.size
      if (n < 2)
        1
      else {
        // Find the smallest shift that makes the list equal to itself
        val smallestPeriod =
          (1 to n).find { shift =>

            // Optimization: We only need to check shifts that divide n
            n % shift == 0 && ring.rotateRight(shift) == ring
          }

        n / smallestPeriod.getOrElse(n)
      }
    }

    /** Finds the indices of each element of this circular sequence close to an axis of reflectional symmetry.
      *
      * @return
      *   the indices of each element of this circular sequence close to an axis of reflectional symmetry,
      *   that is a line of symmetry that splits the sequence in two identical halves.
      * @example
      *   {{{Seq(2, 1, 2, 2, 1, 2, 2, 1, 2, 2, 1, 2).symmetryIndices // List(0, 3, 6, 9)}}}
      */
    def symmetryIndices: List[Index] =
      if (ring.isEmpty) Nil
      else {
        val reversed = ring.reverse

        // We check rotations of the reversed list against the original.
        // If list == rotate(reverse(list), k), it implies an axis of symmetry exists.
        // This is equivalent to counting how many shifts of the reversed list match the original.
        (0 until ring.size).toList.filter { shift =>

          ring == reversed.rotateLeft(shift)
        }
      }

    /** Calculates the axes of reflectional symmetry. Returns a list of pairs of locations where each axis
      * intersects the cycle.
      *
      * @return
      *   A list where each pair represents the two points on the cycle where the axis passes.
      */
    def reflectionalSymmetryAxes: List[(AxisLocation, AxisLocation)] = {
      val n = ring.size

      def edgeIndices(i: Index): AxisLocation =
        Edge(i, (i + 1) % n)

      def oppositeEdgeIndex(i: Index): Index =
        (i + n / 2) % n

      symmetryIndices.map { shift =>

        // The reflection maps index i to (n - 1 - shift - i) % n.
        // Fixed points satisfy 2*i == n - 1 - shift (mod n).
        // Let K = n - 1 - shift.
        val K          = (n - 1 - shift) % n
        val effectiveK = if (K < 0) K + n else K

        if (n % 2 != 0) {
          // Odd n: Equation 2*i = K (mod n) has exactly one solution for vertices.
          // Inverse of 2 mod n is (n + 1) / 2.
          val v               = (effectiveK * (n + 1) / 2) % n
          // The axis must also pass through the midpoint of the opposite edge.
          // Edge index e is the edge starting at (v + n / 2) % n.
          val oppositeEdgeIdx = oppositeEdgeIndex(v)
          (Vertex(v), edgeIndices(oppositeEdgeIdx))
        } else {
          // Even n
          if (effectiveK % 2 == 0) {
            // K is even: 2*i = K (mod n) has two solutions for vertices.
            // i = K / 2 and i = K / 2 + n / 2.
            val v1 = effectiveK / 2
            val v2 = oppositeEdgeIndex(v1)
            (Vertex(v1), Vertex(v2))
          } else {
            // K is odd: No vertex solutions. Axis passes through two edges.
            // The geometric location is K / 2 (half-integer).
            // Corresponding to edge (K - 1) / 2 and opposite edge.
            val e1 = (effectiveK - 1) / 2
            val e2 = oppositeEdgeIndex(e1)
            (edgeIndices(e1), edgeIndices(e2))
          }
        }
      }
    }

    /** Computes the order of reflectional (mirror) symmetry possessed by this circular sequence.
      *
      * @return
      *   the number >= 0 of reflections in which this circular sequence looks exactly the same.
      * @example
      *   {{{Seq(2, 1, 2, 2, 1, 2, 2, 1, 2, 2, 1, 2).symmetry // 4}}}
      */
    def symmetry: Int =
      symmetryIndices.size

  }

  implicit private class SymmetryEnrichment[A, CC[B] <: SeqOps[B, CC, CC[B]]](val ring: CC[A])
      extends AnyVal
      with SymmetryDecorators[A, CC]

}
