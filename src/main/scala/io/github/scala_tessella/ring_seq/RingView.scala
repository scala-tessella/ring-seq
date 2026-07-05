package io.github.scala_tessella.ring_seq

import RingSeq._

import scala.collection.{Factory, IndexedSeq, Seq}
import scala.util.hashing.MurmurHash3

/** A lazy '''view''' of a sequence considered circular.
  *
  * A `RingView` never copies its elements: it wraps an `IndexedSeq` together with a rotation `offset` and a
  * `reflected` flag, so [[rotateRight]], [[rotateLeft]], [[startAt]], [[reflectAt]] and [[reverse]] are all
  * O(1) — they return a new view over the same underlying sequence. In particular [[rotations]] and
  * [[rotationsAndReflections]] produce views in O(1) each, so pipelines like `seq.ring.rotations.exists(...)`
  * short-circuit without materializing anything.
  *
  * Because `RingView` is its own type (not a `Seq`), the circular operations carry their plain names — no `O`
  * suffix is needed: `seq.ring.slice(-1, 4)`, `seq.ring.sliding(2)`, and indexing wraps in both directions.
  *
  * Materialize at the boundary with [[toSeq]], [[toVector]], [[to]] or [[iterator]].
  *
  * Obtain a view with the `ring` method added to every `Seq` (and `String`) by `import RingSeq._`:
  * {{{
  * Vector(0, 1, 2, 3).ring.rotateRight(1).toVector // Vector(3, 0, 1, 2)
  * }}}
  *
  * @note
  *   a view over a mutable sequence reflects later mutations of it, exactly like the standard library views.
  *   A non-indexed source (e.g. `List`) is copied once to a `Vector` on creation.
  */
final class RingView[A] private (underlying: IndexedSeq[A], offset: Int, reflected: Boolean) {

  /** The number of elements in the ring. */
  def size: Int = underlying.length

  /** The number of elements in the ring. */
  def length: Int = underlying.length

  /** Tests whether the ring is empty. */
  def isEmpty: Boolean = underlying.isEmpty

  /** Tests whether the ring is not empty. */
  def nonEmpty: Boolean = underlying.nonEmpty

  // The single source of truth translating a view position in `[0, size)` to an underlying index.
  private def mapIndex(pos: Int): Int =
    if (reflected) (offset - pos + size) % size
    else (offset + pos)                  % size

  /** Normalize a given circular index to `[0, size)`.
    *
    * @throws java.lang.ArithmeticException
    *   if the ring is empty
    */
  def indexFrom(i: IndexO): Index =
    java.lang.Math.floorMod(i, size)

  /** Gets the element at some circular index: any integer is valid, wrapping in both directions.
    *
    * @throws java.lang.ArithmeticException
    *   if the ring is empty
    * @example
    *   {{{Seq(0, 1, 2).ring(3) // 0}}}
    */
  def apply(i: IndexO): A =
    underlying(mapIndex(indexFrom(i)))

  /** Optionally gets the element at some circular index: `None` only when the ring is empty.
    *
    * @example
    *   {{{Seq(0, 1, 2).ring.lift(3) // Some(0)}}}
    */
  def lift(i: IndexO): Option[A] =
    if (isEmpty) None else Some(apply(i))

  /** Finds the circular index of the first element equal to a given value, searching circularly from a given
    * circular index and wrapping past the end.
    *
    * @return
    *   the index in `[0, size)`, or -1 if the element is not in the ring.
    */
  def indexOf(elem: A, from: IndexO = 0): Index =
    toVector.indexOfO(elem, from)

  /** A view of this ring rotated to start at some circular index — O(1), no copy.
    *
    * @example
    *   {{{Seq(0, 1, 2, 3).ring.startAt(2).toVector // Vector(2, 3, 0, 1)}}}
    */
  def startAt(i: IndexO): RingView[A] =
    if (isEmpty) this else new RingView(underlying, mapIndex(indexFrom(i)), reflected)

  /** A view of this ring rotated to the right by some steps (negative = left) — O(1), no copy.
    *
    * @example
    *   {{{Seq(0, 1, 2).ring.rotateRight(1).toVector // Vector(2, 0, 1)}}}
    */
  def rotateRight(step: Int): RingView[A] =
    startAt(-step)

  /** A view of this ring rotated to the left by some steps (negative = right) — O(1), no copy.
    *
    * @example
    *   {{{Seq(0, 1, 2).ring.rotateLeft(1).toVector // Vector(1, 2, 0)}}}
    */
  def rotateLeft(step: Int): RingView[A] =
    startAt(step)

  /** A view of this ring reflected (direction flipped) to start at some circular index — O(1), no copy.
    * Applying `reflectAt` at the same index twice returns the original view.
    *
    * @example
    *   {{{Seq(0, 1, 2).ring.reflectAt().toVector // Vector(0, 2, 1)}}}
    */
  def reflectAt(i: IndexO = 0): RingView[A] =
    if (isEmpty) this else new RingView(underlying, mapIndex(indexFrom(i)), !reflected)

  /** A view of this ring with the elements in reverse order — O(1), no copy. */
  def reverse: RingView[A] =
    reflectAt(-1)

  /** Iterates once over the ring in view order. */
  def iterator: Iterator[A] =
    Iterator.tabulate(size)(pos => underlying(mapIndex(pos)))

  /** Materializes this view to a `Vector`. */
  def toVector: Vector[A] =
    underlying match {
      case v: Vector[A] if offset == 0 && !reflected => v
      case _                                         => Vector.from(iterator)
    }

  /** Materializes this view to an immutable `Seq`. */
  def toSeq: scala.collection.immutable.Seq[A] =
    toVector

  /** Materializes this view to any collection type.
    *
    * @example
    *   {{{Seq(0, 1, 2).ring.rotateLeft(1).to(List) // List(1, 2, 0)}}}
    */
  def to[C1](factory: Factory[A, C1]): C1 =
    iterator.to(factory)

  /** Selects an interval of elements as a lazy iterator: circular indices, and the interval may be longer
    * than the ring itself.
    *
    * @example
    *   {{{Seq(0, 1, 2).ring.slice(-1, 4).toList // List(2, 0, 1, 2, 0)}}}
    */
  def slice(from: IndexO, until: IndexO): Iterator[A] =
    if (isEmpty || from >= until) Iterator.empty
    else {
      val rotated = startAt(from)
      Iterator.continually(rotated).flatMap(_.iterator).take(until - from)
    }

  /** Computes the length of the longest segment starting at some circular index whose elements all satisfy a
    * predicate (at most one full lap).
    */
  def segmentLength(p: A => Boolean, from: IndexO = 0): Int =
    startAt(from).iterator.takeWhile(p).size

  /** Selects the longest prefix of elements starting at some circular index that satisfy a predicate, as a
    * lazy iterator (at most one full lap).
    */
  def takeWhile(p: A => Boolean, from: IndexO = 0): Iterator[A] =
    startAt(from).iterator.takeWhile(p)

  /** Drops the longest prefix of elements starting at some circular index that satisfy a predicate, as a lazy
    * iterator over the remainder of the lap.
    */
  def dropWhile(p: A => Boolean, from: IndexO = 0): Iterator[A] =
    startAt(from).iterator.dropWhile(p)

  /** Splits one lap of the ring at the first element, starting from some circular index, that does not
    * satisfy the predicate.
    *
    * @return
    *   a pair `(takeWhile(p, from), dropWhile(p, from))` of lazy iterators.
    */
  def span(p: A => Boolean, from: IndexO = 0): (Iterator[A], Iterator[A]) =
    startAt(from).iterator.span(p)

  /** Tests whether this ring contains a given sequence as a (possibly wrapping) slice. */
  def containsSlice(that: Seq[A]): Boolean =
    toVector.containsSliceO(that)

  /** Finds the first index at or after a circular start index where this ring contains a given sequence as a
    * (possibly wrapping) slice, or -1 if none exists.
    */
  def indexOfSlice(that: Seq[A], from: IndexO = 0): Index =
    toVector.indexOfSliceO(that, from)

  /** Finds the last index at or before a circular end index where this ring contains a given sequence as a
    * (possibly wrapping) slice, or -1 if none exists.
    */
  def lastIndexOfSlice(that: Seq[A], end: IndexO = -1): Index =
    toVector.lastIndexOfSliceO(that, end)

  /** Groups elements in fixed size blocks by passing a "sliding window" over the ring, wrapping past the end.
    *
    * @example
    *   {{{Seq(0, 1, 2).ring.sliding(2).toList // List(Seq(0, 1), Seq(1, 2), Seq(2, 0))}}}
    */
  def sliding(size: Int, step: Int = 1): Iterator[Seq[A]] =
    slice(0, step * (this.size - 1) + size).sliding(size, step)

  /** Partitions the ring into non-overlapping fixed-size blocks, the last wrapping across the seam so every
    * block has exactly `size` elements. Produces `ceil(n / size)` blocks, none for an empty ring.
    */
  def grouped(size: Int): Iterator[Seq[A]] =
    if (isEmpty) Iterator.empty
    else {
      val count = (this.size + size - 1) / size
      slice(0, count * size).grouped(size)
    }

  /** Iterates over the elements paired with their view index, starting at some circular index. Indices are in
    * `[0, size)`.
    */
  def zipWithIndex(from: IndexO = 0): Iterator[(A, Index)] =
    if (isEmpty) Iterator.empty
    else {
      val n     = size
      val start = indexFrom(from)
      startAt(from).iterator.zipWithIndex.map { case (a, i) =>
        (a, (start + i) % n)
      }
    }

  /** All the rotations of this ring as views — O(1) each, starting from this view itself, or just itself if
    * empty.
    *
    * @example
    *   {{{Seq(0, 1, 2).ring.rotations.map(_.toVector) // Iterator(Vector(0, 1, 2), Vector(1, 2, 0), Vector(2, 0, 1))}}}
    */
  def rotations: Iterator[RingView[A]] =
    if (isEmpty) Iterator(this) else Iterator.tabulate(size)(startAt)

  /** The 2 reflections of this ring as views (itself and `reflectAt()`), or just itself if empty. */
  def reflections: Iterator[RingView[A]] =
    if (isEmpty) Iterator(this) else Iterator(this, reflectAt())

  /** The 2 reversions of this ring as views (itself and `reverse`), or just itself if empty. */
  def reversions: Iterator[RingView[A]] =
    if (isEmpty) Iterator(this) else Iterator(this, reverse)

  /** All the rotations and reflections of this ring as views — O(1) each, or just itself if empty. */
  def rotationsAndReflections: Iterator[RingView[A]] =
    if (isEmpty) Iterator(this) else reflections.flatMap(_.rotations)

  /** Tests whether this ring is a rotation of a given sequence. */
  def isRotationOf(that: Seq[A]): Boolean =
    toVector.isRotationOf(that.toVector)

  /** Tests whether this ring is a reflection of a given sequence. */
  def isReflectionOf(that: Seq[A]): Boolean =
    toVector.isReflectionOf(that.toVector)

  /** Tests whether this ring is a reversion of a given sequence. */
  def isReversionOf(that: Seq[A]): Boolean =
    toVector.isReversionOf(that.toVector)

  /** Tests whether this ring is a rotation or a reflection of a given sequence. */
  def isRotationOrReflectionOf(that: Seq[A]): Boolean =
    toVector.isRotationOrReflectionOf(that.toVector)

  /** Finds the rotation offset that aligns this ring with a given sequence.
    *
    * @return
    *   `Some(k)` such that `startAt(k)` equals ''that'', or `None` if no rotation matches (or sizes differ).
    */
  def alignTo(that: Seq[A]): Option[Index] =
    toVector.alignTo(that.toVector)

  /** The number of positions at which corresponding elements differ (Hamming distance).
    *
    * @throws java.lang.IllegalArgumentException
    *   if the sizes differ
    */
  def hammingDistance(that: Seq[A]): Int =
    toVector.hammingDistance(that.toVector)

  /** The minimum Hamming distance over all rotations of this ring.
    *
    * @throws java.lang.IllegalArgumentException
    *   if the sizes differ
    */
  def minRotationalHammingDistance(that: Seq[A]): Int =
    toVector.minRotationalHammingDistance(that.toVector)

  /** The starting index of the lexicographically smallest rotation of this view (two-pointer minimal
    * rotation, O(n) time).
    */
  def canonicalIndex(implicit ord: Ordering[A]): Index =
    if (size <= 1) 0 else NecklaceOps.leastRotation(toVector)

  /** The lexicographically smallest rotation of this ring (necklace canonical form) as a view. */
  def canonical(implicit ord: Ordering[A]): RingView[A] =
    startAt(canonicalIndex)

  /** The lexicographically smallest representative under both rotation and reflection (bracelet canonical
    * form) as a view.
    */
  def bracelet(implicit ord: Ordering[A]): RingView[A] = {
    val a  = canonical
    val b  = reflectAt().canonical
    val vo = Ordering.Implicits.seqOrdering[Vector, A]
    if (vo.lteq(a.toVector, b.toVector)) a else b
  }

  /** Computes the order of rotational symmetry possessed by this ring. */
  def rotationalSymmetry: Int =
    toVector.rotationalSymmetry

  /** Finds the indices of each element of this ring close to an axis of reflectional symmetry. */
  def symmetryIndices: List[Index] =
    toVector.symmetryIndices

  /** Calculates the axes of reflectional symmetry of this ring. */
  def reflectionalSymmetryAxes: List[(AxisLocation, AxisLocation)] =
    toVector.reflectionalSymmetryAxes

  /** Computes the order of reflectional (mirror) symmetry possessed by this ring. */
  def symmetry: Int =
    toVector.symmetry

  /** Two views are equal iff they present the same elements in the same order, regardless of how they were
    * obtained.
    */
  override def equals(other: Any): Boolean =
    other match {
      case that: RingView[_] =>
        (this eq that) || (this.size == that.size && this.iterator.sameElements(that.iterator))
      case _                 => false
    }

  override def hashCode: Int =
    MurmurHash3.orderedHash(iterator)

  override def toString: String =
    iterator.mkString("RingView(", ", ", ")")

}

object RingView {

  /** Creates a view over a source sequence: no copy if the source is an `IndexedSeq`, otherwise a one-time
    * copy to a `Vector`.
    */
  def from[A](source: IterableOnce[A]): RingView[A] =
    source match {
      case is: IndexedSeq[A] => new RingView(is, 0, reflected = false)
      case other             => new RingView(Vector.from(other), 0, reflected = false)
    }

  def apply[A](elems: A*): RingView[A] =
    from(elems)

}
