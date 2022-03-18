package ringseq.examples

import ringseq.RingSeq._

class Ring[A](underlying: Seq[A]) {

  import Ring.BooleanEnrichment

  var headIndex: IndexO = 0

  var isReversed: Boolean = false

  def rotateR(step: Int = 1): Unit =
    headIndex += step * isReversed.toDirection

  def rotateL(step: Int = 1): Unit =
    rotateR(-step)

  def reverse(): Unit =
    isReversed = !isReversed

  def currentHead: A =
    underlying.applyO(headIndex)

  def current: Seq[A] =
    if (isReversed) underlying.reflectAt(headIndex) else underlying.startAt(headIndex)

}

object Ring {

  implicit class BooleanEnrichment(boolean: Boolean) {

    def toDirection: Int = if (boolean) 1 else -1

  }

}
