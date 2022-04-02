package io.github.scala_tessella.ring_seq

import scala.collection.{Seq, SeqOps}

trait SlicingOps extends TransformingOps:

  /** Extension providing decorators for a `Seq` considered circular. */
  extension[A, CC[B] <: SeqOps[B, CC, CC[B]]](ring: CC[A])

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
      ring.startAt(from).segmentLength(p)

    private def emptied: CC[A] =
      ring.take(0)

    private def multiply(times: Int): CC[A] =
      (0 until times).foldLeft(emptied)((acc, _) => acc ++ ring)

    /** Selects an interval of elements.
     *
     * @param from  [[IndexO]]
     * @param until [[IndexO]]
     * @return a sequence containing the elements greater than or equal to circular index ''from''
     *         extending up to (but not including) circular index ''until'' of this sequence.
     * @note a slice of a circular sequence can be bigger than the size of the elements in the sequence.
     * @example {{{Seq(0, 1, 2).sliceO(-1, 4) // Seq(2, 0, 1, 2, 0)}}}
     */
    def sliceO(from: IndexO, to: IndexO): CC[A] =
      if ring.isEmpty then ring
      else if from >= to then emptied
      else
       val length = to - from
       val times = Math.ceil(length / ring.size).toInt + 1
       ring.startAt(from).multiply(times).take(length)

    private def growBy(growth: Int): CC[A] =
      sliceO(0, ring.size + growth)

    /** Tests whether this circular sequence contains a given sequence as a slice.
     *
     * @param that the sequence to test
     * @return true if this circular sequence contains a slice with the same elements as ''that'',
     *         otherwise false.
     * @example {{{Seq(0, 1, 2).containsSliceO(Seq(2, 0, 1, 2, 0)) // true}}}
     */
    def containsSliceO(slice: Seq[A]): Boolean =
      growBy(slice.size - 1).containsSlice(slice)

    /** Finds first index after or at a start index where this circular sequence contains a given sequence as a slice.
     *
     * @param that the sequence to test
     * @param from [[IndexO]]
     * @return the first index >= ''from'' such that the elements of this circular sequence starting at this index
     *         match the elements of sequence ''that'',
     *         or -1 if no such subsequence exists.
     * @example {{{Seq(0, 1, 2).indexOfSliceO(Seq(2, 0, 1, 2, 0)) // 2}}}
     */
    def indexOfSliceO(that: Seq[A], from: IndexO = 0): Index =
      val grown = growBy(that.size - 1)
      grown.indexOfSlice(that, grown.indexFrom(from))

    /** Finds last index before or at a given end index where this circular sequence contains a given sequence as a slice.
     *
     * @param that the sequence to test
     * @param end  [[IndexO]]
     * @return the last index <= ''end'' such that the elements of this circular sequence starting at this index
     *         match the elements of sequence ''that'',
     *         or -1 if no such subsequence exists.
     * @example {{{Seq(0, 1, 2, 0, 1, 2).lastIndexOfSliceO(Seq(2, 0)) // 5}}}
     */
    def lastIndexOfSliceO(that: Seq[A], end: IndexO = -1): Index =
      val grown = growBy(that.size - 1)
      grown.lastIndexOfSlice(that, grown.indexFrom(end))
