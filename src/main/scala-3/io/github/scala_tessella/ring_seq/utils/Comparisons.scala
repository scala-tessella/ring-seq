package io.github.scala_tessella.ring_seq.utils

import scala.collection.Seq

/** Contains methods to compare sequences.
 *
 * @author Mario Càllisto
 */
object Comparisons {

  /** Test if two sequences have the same size using `sizeCompare`
   *
   * @param seq1 a sequence
   * @param seq2 another sequence
   * @return if the sequences have the same size `true`, else `false`
   */
  def areSameSize(seq1: Seq[_], seq2: Seq[_]): Boolean =
    seq1.sizeCompare(seq2) == 0

}
