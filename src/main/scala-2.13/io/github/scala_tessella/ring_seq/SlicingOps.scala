package io.github.scala_tessella.ring_seq

import scala.collection.SeqOps

trait SlicingOps[A, CC[B] <: SeqOps[B, CC, CC[B]]] extends Any with TransformingOps[A, CC] {

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

  private def emptied: CC[A] =
    ring.take(0)

  private def multiply(seq: CC[A], times: Int): CC[A] =
    (0 until times).foldLeft(emptied)((acc, _) => acc ++ seq)

  /** Selects an interval of elements.
   *
   * @param from  [[IndexO]]
   * @param until [[IndexO]]
   * @return a sequence containing the elements greater than or equal to circular index ''from''
   *         extending up to (but not including) circular index ''until'' of this sequence.
   * @note a slice of a circular sequence can be bigger than the size of the elements in the sequence.
   * @example {{{Seq(0, 1, 2).sliceO(-1, 4) // Seq(2, 0, 1, 2, 0)}}}
   */
  def sliceO(from: IndexO, until: IndexO): CC[A] = {
    if (ring.isEmpty) ring
    else if (from >= until) emptied
    else {
      val length = until - from
      val times = Math.ceil(length / ring.size).toInt + 1
      multiply(startAt(from), times).take(length)
    }
  }

}
