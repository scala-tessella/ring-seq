/**
 * Adds methods to [[https://www.scala-lang.org/api/current/scala/collection/immutable/Seq.html scala.collection.immutable.Seq]]
 * (and subtypes) when considered circular, its elements forming a ring network.
 * @author Mario Càllisto
 */
object RingSeq {

  /**
   * For improved readability, the index of a `Seq`.
   */
  type Index = Int

  /**
   * For improved readability, the index of a circular `Seq`.
   * @note any value is valid, provided the `Seq` is not empty
   */
  type IndexO = Int

  private def floor(i: IndexO, size: Int): Index =
    java.lang.Math.floorMod(i, size)

  private def emptied[A](seq: Seq[A]): Seq[A] =
    seq.take(0)

  private def multiply[A](seq: Seq[A], times: Int): Seq[A] =
    (0 until times).foldLeft(emptied(seq))((acc, _) => acc ++ seq)

  private def greaterHalfRange(size: Int): Range =
    0 until Math.ceil(size / 2.0).toInt

  private def checkReflectionAxis[A](seq: Seq[A], gap: Int): Boolean =
    greaterHalfRange(seq.size).forall(j => seq.applyO(j + 1) == seq.applyO(-(j + gap)))

  private def hasHeadOnAxis[A](seq: Seq[A]): Boolean =
    checkReflectionAxis(seq, 1)

  private def hasAxisBetweenHeadAndNext[A](seq: Seq[A]): Boolean =
    checkReflectionAxis(seq, 0)

  private def findReflectionSymmetry[A](seq: Seq[A]): Option[Index] =
    greaterHalfRange(seq.size).find(j => {
      val rotation = seq.startAt(j)
      hasHeadOnAxis(rotation) || hasAxisBetweenHeadAndNext(rotation)
    })

  /**
   * Decorators for a `Seq` considered circular.
   */
  trait RingDecorators[A] extends Any {

    def ring: Seq[A]

    private def index(i: IndexO): Index =
      floor(i, ring.size)

    /**
     * Gets the element at some circular index.
     * @param i [[IndexO]]
     * @throws java.lang.ArithmeticException if `Seq` is empty
     * @example {{{Seq(0, 1, 2).applyO(3) // 0}}}
     */
    def applyO(i: IndexO): A =
      ring(index(i))

    /**
     * Rotate the sequence to the right by some steps.
     * @param step Int
     * @return a sequence consisting of all elements rotated to the right by ''step'' places.
     *         If ''step'' is negative the rotation happens to the left.
     * @example {{{Seq(0, 1, 2).rotateRight(1) // Seq(2, 0, 1)}}}
     */
    def rotateRight(step: Int): Seq[A] =
      if (ring.isEmpty) ring
      else {
        val j: Index = ring.size - index(step)
        ring.drop(j) ++ ring.take(j)
      }

    /**
     * Rotates the sequence to the left by some steps.
     * @param step Int
     * @return a sequence consisting of all elements rotated to the left by ''step'' places.
     *         If ''step'' is negative the rotation happens to the right.
     * @example {{{Seq(0, 1, 2).rotateLeft(1) // Seq(1, 2, 0)}}}
     */
    def rotateLeft(step: Int): Seq[A] =
      rotateRight(-step)

    /**
     * Rotates the sequence to start at some circular index.
     * @param i [[IndexO]]
     * @return a sequence consisting of all elements rotated to start at circular index ''i''.
     *         It is equivalent to [[rotateLeft]].
     * @example {{{Seq(0, 1, 2).startAt(1) // Seq(1, 2, 0)}}}
     */
    def startAt(i: IndexO): Seq[A] =
      rotateLeft(i)

    /**
     * Reflects the sequence to start at some circular index.
     * @param i [[IndexO]]
     * @return a sequence consisting of all elements reversed and rotated to start at circular index ''i''.
     * @example {{{Seq(0, 1, 2).reflectAt() // Seq(0, 2, 1)}}}
     */
    def reflectAt(i: IndexO = 0): Seq[A] =
      startAt(i + 1).reverse

    /**
     * Computes the length of the longest segment that starts from some circular index
     * and whose elements all satisfy some predicate.
     * @param p the predicate used to test elements
     * @param from [[IndexO]]
     * @return the length of the longest segment of this sequence starting from circular index ''from''
     *         such that every element of the segment satisfies the predicate ''p''
     * @example {{{Seq(0, 1, 2).segmentLengthO(_ % 2 == 0, 2) // 2}}}
     */
    def segmentLengthO(p: A => Boolean, from: IndexO = 0): Int =
      startAt(from).segmentLength(p)

    /**
     * Selects an interval of elements.
     * @param from [[IndexO]]
     * @param until [[IndexO]]
     * @return a sequence containing the elements greater than or equal to circular index ''from''
     *         extending up to (but not including) circular index ''until'' of this sequence.
     * @note a slice of a circular sequence can be bigger than the size of the elements in the sequence.
     * @example {{{Seq(0, 1, 2).sliceO(-1, 4) // Seq(2, 0, 1, 2, 0)}}}
     */
    def sliceO(from: IndexO, until: IndexO): Seq[A] = {
      if (ring.isEmpty) ring
      else if (from >= until) emptied(ring)
      else {
        val length = until - from
        val times = Math.ceil(length / ring.size).toInt + 1
        multiply(startAt(from), times).take(length)
      }
    }

    private def growBy(growth: Int): Seq[A] =
      sliceO(0, ring.size + growth)

    /**
     * Tests whether this circular sequence contains a given sequence as a slice.
     * @param that the sequence to test
     * @return true if this circular sequence contains a slice with the same elements as ''that'',
     *         otherwise false.
     * @example {{{Seq(0, 1, 2).containsSliceO(Seq(2, 0, 1, 2, 0)) // true}}}
     */
    def containsSliceO(that: Seq[A]): Boolean =
      growBy(that.size - 1).containsSlice(that)

    /**
     * Finds first index after or at a start index where this circular sequence contains a given sequence as a slice.
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

    /**
     * Finds last index before or at a given end index where this circular sequence contains a given sequence as a slice.
     * @param that the sequence to test
     * @param end [[IndexO]]
     * @return the last index <= ''end'' such that the elements of this immutable sequence starting at this index
     *         match the elements of sequence ''that'',
     *         or -1 if no such subsequence exists.
     * @example {{{Seq(0, 1, 2, 0, 1, 2).lastIndexOfSliceO(Seq(2, 0)) // 5}}}
     */
    def lastIndexOfSliceO(that: Seq[A], end: IndexO = -1): Index = {
      val grown = growBy(that.size - 1)
      grown.lastIndexOfSlice(that, floor(end, grown.size))
    }

    /**
     * Groups elements in fixed size blocks by passing a "sliding window" over them
     * @param size the number of elements per group
     * @param step the distance between the first elements of successive groups
     * @return An iterator producing sequences of size ''size''.
     * @example {{{Seq(0, 1, 2).slidingO(2) // Iterator(Seq(0, 1), Seq(1, 2), Seq(2, 0))}}}
     */
    def slidingO(size: Int, step: Int = 1): Iterator[Seq[A]] =
      sliceO(0, step * (ring.size - 1) + size).sliding(size, step)

    private def transformations(f: Seq[A] => Iterator[Seq[A]]): Iterator[Seq[A]] =
      if (ring.isEmpty) Iterator(ring) else f(ring)

    def rotations: Iterator[Seq[A]] =
      transformations(r => slidingO(r.size))

    def reflections: Iterator[Seq[A]] =
      transformations(r => List(r, r.reflectAt()).iterator)

    def reversions: Iterator[Seq[A]] =
      transformations(r => List(r, r.reverse).iterator)

    def rotationsAndReflections: Iterator[Seq[A]] =
      transformations(_.reflections.flatMap(_.rotations))

    def minRotation(implicit ordering: Ordering[Seq[A]]): Seq[A] =
      rotations.min(ordering)

    private def isTransformationOf(other: Seq[A], f: Seq[A] => Iterator[Seq[A]]): Boolean =
      ring.sizeCompare(other) == 0 && f(ring).contains(other)

    def isRotationOf(other: Seq[A]): Boolean =
      isTransformationOf(other, _.rotations)

    def isReflectionOf(other: Seq[A]): Boolean =
      isTransformationOf(other, _.reflections)

    def isRotationOrReflectionOf(other: Seq[A]): Boolean =
      isTransformationOf(other, _.rotationsAndReflections)

    private def areFoldsSymmetrical: Int => Boolean =
      n => rotateRight(ring.size / n) == ring

    def rotationalSymmetry: Int = {
      val size = ring.size
      if (size < 2) 1
      else {
        val exactFoldsDesc = size +: (size / 2 to 2 by -1).filter(size % _ == 0)
        exactFoldsDesc.find(areFoldsSymmetrical).getOrElse(1)
      }
    }

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

    def symmetry: Int =
      symmetryIndices.size  }

  /**
   * Provides methods for a generic `Seq` considered circular.
   */
  implicit class RingSeqEnrichment[A](val ring: Seq[A]) extends AnyVal with RingDecorators[A]

  /**
   * Provides methods for a String considered circular.
   */
  implicit class RingStringEnrichment(val s: String) extends AnyVal with RingDecorators[Char] {

    def ring: Seq[Char] = s.toSeq

  }

}
