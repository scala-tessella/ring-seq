package ringseq.examples

import ringseq.RingSeq._

class Ring[A](underlying: Seq[A]) {

  import Ring.BooleanEnrichment

  var headIndex: IndexO = 0

  var isReflected: Boolean = false

  def rotateR(step: Int = 1): Unit =
    headIndex += step * isReflected.toDirection

  def rotateL(step: Int = 1): Unit =
    rotateR(-step)

  def reflect(): Unit =
    isReflected = !isReflected

  def currentHead: A =
    underlying.applyO(headIndex)

  def current: Seq[A] =
    if (isReflected) underlying.reflectAt(headIndex) else underlying.startAt(headIndex)

}

object Ring {

  implicit class BooleanEnrichment(boolean: Boolean) {

    def toDirection: Int = if (boolean) 1 else -1

  }

}
