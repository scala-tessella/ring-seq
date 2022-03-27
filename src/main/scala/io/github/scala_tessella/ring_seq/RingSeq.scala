package io.github.scala_tessella.ring_seq

import scala.collection.{Seq, SeqOps}
//import utils.Comparisons.areSameSize

/** Adds implicit methods to `[[https://www.scala-lang.org/api/current/scala/collection/Seq.html Seq]]`
 * (immutable / mutable and subtypes) for when a sequence needs to be considered '''circular''', its elements forming a ring.
 *
 * @author Mario Càllisto
 */
object RingSeq {

  /** For improved readability, the index of a `Seq`. */
  type Index = Int

  /** For improved readability, the index of a circular `Seq`.
   *
   * @note any value is a valid index, provided that `Seq` is not empty
   */
  type IndexO = Int

  private def floor(i: IndexO, size: Int): Index =
    java.lang.Math.floorMod(i, size)

  private def emptied[A, B[A] <: SeqOps[A, B, B[A]]](seq: B[A]): B[A] =
    seq.take(0)

  private def multiply[A, B[A] <: SeqOps[A, B, B[A]]](seq: B[A], times: Int): B[A] =
    (0 until times).foldLeft(emptied(seq))((acc, _) => acc ++ seq)

  private def greaterHalfRange(size: Int): Range =
    0 until Math.ceil(size / 2.0).toInt

  private def checkReflectionAxis[A, B[A] <: SeqOps[A, B, B[A]]](seq: B[A], gap: Int): Boolean =
    greaterHalfRange(seq.size).forall(j => seq.applyO(j + 1) == seq.applyO(-(j + gap)))

  private def hasHeadOnAxis[A, B[A] <: SeqOps[A, B, B[A]]](seq: B[A]): Boolean =
    checkReflectionAxis(seq, 1)

  private def hasAxisBetweenHeadAndNext[A, B[A] <: SeqOps[A, B, B[A]]](seq: B[A]): Boolean =
    checkReflectionAxis(seq, 0)

  private def findReflectionSymmetry[A, B[A] <: SeqOps[A, B, B[A]]](seq: B[A]): Option[Index] =
    greaterHalfRange(seq.size).find(j => {
      val rotation = seq.startAt(j)
      hasHeadOnAxis(rotation) || hasAxisBetweenHeadAndNext(rotation)
    })

  /** Universal trait providing decorators for a `Seq` considered circular. */
  trait RingDecorators[A, B[A] <: SeqOps[A, B, B[A]]] extends Any {

    /** The circular sequence */
    def ring: B[A]

    private def index(i: IndexO): Index =
      floor(i, ring.size)

    /** Gets the element at some circular index.
     *
     * @param i [[IndexO]]
     * @throws java.lang.ArithmeticException if `Seq` is empty
     * @example {{{Seq(0, 1, 2).applyO(3) // 0}}}
     */
    def applyO(i: IndexO): A =
      ring(index(i))

    /** Rotate the sequence to the right by some steps.
     *
     * @param step the circular distance between each new and old position
     * @return a sequence consisting of all elements rotated to the right by ''step'' places.
     *         If ''step'' is negative the rotation happens to the left.
     * @example {{{Seq(0, 1, 2).rotateRight(1) // Seq(2, 0, 1)}}}
     */
    def rotateRight(step: Int): B[A] =
      if (ring.isEmpty) ring
      else {
        val j: Index = ring.size - index(step)
        ring.drop(j) ++ ring.take(j)
      }

    /** Rotates the sequence to the left by some steps.
     *
     * @param step the circular distance between each old and new position
     * @return a sequence consisting of all elements rotated to the left by ''step'' places.
     *         If ''step'' is negative the rotation happens to the right.
     * @example {{{Seq(0, 1, 2).rotateLeft(1) // Seq(1, 2, 0)}}}
     */
    def rotateLeft(step: Int): B[A] =
      rotateRight(-step)

    /** Rotates the sequence to start at some circular index.
     *
     * @param i [[IndexO]]
     * @return a sequence consisting of all elements rotated to start at circular index ''i''.
     *         It is equivalent to [[rotateLeft]].
     * @example {{{Seq(0, 1, 2).startAt(1) // Seq(1, 2, 0)}}}
     */
    def startAt(i: IndexO): B[A] =
      rotateLeft(i)

    /** Reflects the sequence to start at some circular index.
     *
     * @param i [[IndexO]]
     * @return a sequence consisting of all elements reversed and rotated to start at circular index ''i''.
     * @example {{{Seq(0, 1, 2).reflectAt() // Seq(0, 2, 1)}}}
     */
    def reflectAt(i: IndexO = 0): B[A] =
      startAt(i + 1).reverse

    /** Computes the length of the longest segment that starts from some circular index
     * and whose elements all satisfy some predicate.
     *
     * @param p    the predicate used to test elements
     * @param from [[IndexO]]
     * @return the length of the longest segment of this sequence starting from circular index ''from''
     *         such that every element of the segment satisfies the predicate ''p''
     * @example {{{Seq(0, 1, 2).segmentLengthO(_ % 2 == 0, 2) // 2}}}
     */
    def segmentLengthO(p: A => Boolean, from: IndexO = 0): Int =
      startAt(from).segmentLength(p, 0)

    /** Selects an interval of elements.
     *
     * @param from  [[IndexO]]
     * @param until [[IndexO]]
     * @return a sequence containing the elements greater than or equal to circular index ''from''
     *         extending up to (but not including) circular index ''until'' of this sequence.
     * @note a slice of a circular sequence can be bigger than the size of the elements in the sequence.
     * @example {{{Seq(0, 1, 2).sliceO(-1, 4) // Seq(2, 0, 1, 2, 0)}}}
     */
    def sliceO(from: IndexO, until: IndexO): B[A] = {
      if (ring.isEmpty) ring
      else if (from >= until) emptied(ring)
      else {
        val length = until - from
        val times = Math.ceil(length / ring.size).toInt + 1
        multiply(startAt(from), times).take(length)
      }
    }

    private def growBy(growth: Int): B[A] =
      sliceO(0, ring.size + growth)

    /** Tests whether this circular sequence contains a given sequence as a slice.
     *
     * @param that the sequence to test
     * @return true if this circular sequence contains a slice with the same elements as ''that'',
     *         otherwise false.
     * @example {{{Seq(0, 1, 2).containsSliceO(Seq(2, 0, 1, 2, 0)) // true}}}
     */
    def containsSliceO(that: Seq[A]): Boolean =
      growBy(that.size - 1).containsSlice(that)

    /** Finds first index after or at a start index where this circular sequence contains a given sequence as a slice.
     *
     * @param that the sequence to test
     * @param from [[IndexO]]
     * @return the first index >= ''from'' such that the elements of this circular sequence starting at this index
     *         match the elements of sequence ''that'',
     *         or -1 if no such subsequence exists.
     * @example {{{Seq(0, 1, 2).indexOfSliceO(Seq(2, 0, 1, 2, 0)) // 2}}}
     */
    def indexOfSliceO(that: Seq[A], from: IndexO = 0): Index = {
      val grown = growBy(that.size - 1)
      grown.indexOfSlice(that, floor(from, grown.size))
    }

    /** Finds last index before or at a given end index where this circular sequence contains a given sequence as a slice.
     *
     * @param that the sequence to test
     * @param end  [[IndexO]]
     * @return the last index <= ''end'' such that the elements of this circular sequence starting at this index
     *         match the elements of sequence ''that'',
     *         or -1 if no such subsequence exists.
     * @example {{{Seq(0, 1, 2, 0, 1, 2).lastIndexOfSliceO(Seq(2, 0)) // 5}}}
     */
    def lastIndexOfSliceO(that: Seq[A], end: IndexO = -1): Index = {
      val grown = growBy(that.size - 1)
      grown.lastIndexOfSlice(that, floor(end, grown.size))
    }

    /** Groups elements in fixed size blocks by passing a "sliding window" over them
     *
     * @param size the number of elements per group
     * @param step the distance between the first elements of successive groups
     * @return An iterator producing sequences of size ''size''.
     * @example {{{Seq(0, 1, 2).slidingO(2) // Iterator(Seq(0, 1), Seq(1, 2), Seq(2, 0))}}}
     */
    def slidingO(size: Int, step: Int = 1): Iterator[B[A]] =
      sliceO(0, step * (ring.size - 1) + size).sliding(size, step)

    private def transformations(f: B[A] => Iterator[B[A]]): Iterator[B[A]] =
      if (ring.isEmpty) Iterator(ring) else f(ring)

    /** Computes all the rotations of this circular sequence
     *
     * @return An iterator producing all the sequences obtained by rotating this circular sequence,
     *         starting from itself and moving one rotation step to the right,
     *         or just itself if empty.
     * @example {{{Seq(0, 1, 2).rotations // Iterator(Seq(0, 1, 2), Seq(1, 2, 0), Seq(2, 0, 1))}}}
     */
    def rotations: Iterator[B[A]] =
      transformations(r => slidingO(r.size))

    /** Computes all the reflections of this circular sequence
     *
     * @return An iterator producing the 2 sequences obtained by reflecting this circular sequence,
     *         starting from itself,
     *         or just itself if empty.
     * @example {{{Seq(0, 1, 2).reflections // Iterator(Seq(0, 1, 2), Seq(0, 2, 1))}}}
     */
    def reflections: Iterator[B[A]] =
      transformations(r => List(r, r.reflectAt()).iterator)

    /** Computes all the reversions of this circular sequence
     *
     * @return An iterator producing the 2 sequences obtained by reversing this circular sequence,
     *         starting from itself,
     *         or just itself if empty.
     * @example {{{Seq(0, 1, 2).reversions // Iterator(Seq(0, 1, 2), Seq(2, 1, 0))}}}
     */
    def reversions: Iterator[B[A]] =
      transformations(r => List(r, r.reverse).iterator)

    /** Computes all the rotations and reflections of this circular sequence
     *
     * @return An iterator producing all the sequences obtained by rotating and reflecting this circular sequence,
     *         starting from itself and moving one rotation step to the right, then reflecting and doing the same,
     *         or just itself if empty.
     * @example {{{Seq(0, 1, 2).rotationsAndReflections // Iterator(Seq(0, 1, 2), Seq(1, 2, 0), Seq(2, 0, 1), Seq(0, 2, 1), Seq(2, 1, 0), Seq(1, 0, 2))}}}
     */
    def rotationsAndReflections: Iterator[B[A]] =
      transformations(_.reflections.flatMap(_.rotations))

    private def isTransformationOf(that: B[A], f: B[A] => Iterator[B[A]]): Boolean = {
      ring.size == that.size && f(ring).contains(that)
    }

    /** Tests whether this circular sequence is a rotation of a given sequence.
     *
     * @param that the sequence to test
     * @return true if this circular sequence is a rotation of ''that'',
     *         otherwise false.
     * @example {{{Seq(0, 1, 2).isRotationOf(Seq(1, 2, 0)) // true}}}
     */
    def isRotationOf(that: B[A]): Boolean =
      isTransformationOf(that, _.rotations)

    /** Tests whether this circular sequence is a reflection of a given sequence.
     *
     * @param that the sequence to test
     * @return true if this circular sequence is a reflection of ''that'',
     *         otherwise false.
     * @example {{{Seq(0, 1, 2).isReflectionOf(Seq(0, 2, 1)) // true}}}
     */
    def isReflectionOf(that: B[A]): Boolean =
      isTransformationOf(that, _.reflections)

    /** Tests whether this circular sequence is a reversion of a given sequence.
     *
     * @param that the sequence to test
     * @return true if this circular sequence is a reversion of ''that'',
     *         otherwise false.
     * @example {{{Seq(0, 1, 2).isReversionOf(Seq(2, 1, 0)) // true}}}
     */
    def isReversionOf(that: B[A]): Boolean =
      isTransformationOf(that, _.reversions)

    /** Tests whether this circular sequence is a rotation or a reflection of a given sequence.
     *
     * @param that the sequence to test
     * @return true if this circular sequence is a rotation or a reflection of ''that'',
     *         otherwise false.
     * @example {{{Seq(0, 1, 2).isRotationOrReflectionOf(Seq(2, 0, 1)) // true}}}
     */
    def isRotationOrReflectionOf(that: B[A]): Boolean =
      isTransformationOf(that, _.rotationsAndReflections)

    private def areFoldsSymmetrical: Int => Boolean =
      n => rotateRight(ring.size / n) == ring

    /** Computes the order of rotational symmetry possessed by this circular sequence.
     *
     * @return the number >= 1 of rotations in which this circular sequence looks exactly the same.
     * @example {{{Seq(0, 1, 2, 0, 1, 2).rotationalSymmetry // 2}}}
     */
    def rotationalSymmetry: Int = {
      val size = ring.size
      if (size < 2) 1
      else {
        val exactFoldsDesc = size +: (size / 2 to 2 by -1).filter(size % _ == 0)
        exactFoldsDesc.find(areFoldsSymmetrical).getOrElse(1)
      }
    }

    /** Finds the indices of each element of this circular sequence close to an axis of reflectional symmetry.
     *
     * @return the indices of each element of this circular sequence close to an axis of reflectional symmetry,
     *         that is a line of symmetry that splits the sequence in two identical halves.
     * @example {{{Seq(2, 1, 2, 2, 1, 2, 2, 1, 2, 2, 1, 2).symmetryIndices // List(1, 4, 7, 10)}}}
     */
    def symmetryIndices: List[Index] =
      if (ring.isEmpty) Nil
      else {
        val folds = rotationalSymmetry
        val foldSize = ring.size / folds
        findReflectionSymmetry(ring.take(foldSize)) match {
          case None => Nil
          case Some(j) => (0 until folds).toList.map(_ * foldSize + j)
        }
      }

    /** Computes the order of reflectional (mirror) symmetry possessed by this circular sequence.
     *
     * @return the number >= 0 of reflections in which this circular sequence looks exactly the same.
     * @example {{{Seq(2, 1, 2, 2, 1, 2, 2, 1, 2, 2, 1, 2).symmetry // 4}}}
     */
    def symmetry: Int =
      symmetryIndices.size

  }

  /** Value class providing methods for a generic `Seq` considered circular. */
  implicit class RingSeqEnrichment[A, B[A] <: SeqOps[A, B, B[A]]](val ring: B[A]) extends AnyVal with RingDecorators[A, B]

//  /** Value class providing methods for a `String` considered circular. */
//  implicit class RingStringEnrichment(private val s: String) extends AnyVal with RingDecorators[Char] {
//
//    /** Converts this string into a circular `Seq`.
//     *
//     * @return the string as a sequence of `Char`.
//     */
//    def ring: Seq[Char] = s.toSeq
//
//  }

}
