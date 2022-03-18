package ringseq.examples

import ringseq.RingSeq._

/** An example class wrapping an immutable sequence and keeping a mutable state of rotation and reflection
 *
 * @param underlying the wrapped immutable sequence
 * @param headIndex mutable state of rotation, a circular index of the sequence head
 * @param isReflected mutable state of reflection
 * @tparam A the type of the elements in the sequence
 */
class Ring[A](underlying: Seq[A], var headIndex: IndexO = 0, var isReflected: Boolean = false ) {

  private def directionMultiplier: Int =
    if (isReflected) 1 else -1

  /** Adds a rotation to the right by some steps.
   *
   * @param step Int
   */
  def rotateR(step: Int = 1): Unit =
    headIndex += step * directionMultiplier

  /** Adds a rotation to the left by some steps.
   *
   * @param step Int
   */
  def rotateL(step: Int = 1): Unit =
    rotateR(-step)

  /** Adds a reflection, the sequence is considered as flowing in the opposite direction. */
  def reflect(): Unit =
    isReflected = !isReflected

  /** Gets the head element of the sequence at the current state of rotation and reflection.
   *
   * @return the element at the head of the rotated and reflected sequence.
   */
  def currentHead: A =
    underlying.applyO(headIndex)

  /** Gets the sequence at the current state of rotation and reflection.
   *
   * @return the rotated and reflected sequence.
   */
  def current: Seq[A] =
    if (isReflected) underlying.reflectAt(headIndex) else underlying.startAt(headIndex)

}
