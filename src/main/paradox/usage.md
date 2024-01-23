# Usage examples

This page shows examples of how the library can be used.

##  `Ring[A]` class

An example class wrapping a sequence and keeping mutable states of rotation and reflection.

Thanks to the primitives available, it can be built in few lines of code:

```scala
// using type IndexO to signal a circular index
class Ring[A](underlying: Seq[A], var headIndex: IndexO = 0, var isReflected: Boolean = false ) {

  private def directionMultiplier: Int =
    if (isReflected) 1 else -1

  def rotateR(step: Int = 1): Unit =
    headIndex += step * directionMultiplier

  def rotateL(step: Int = 1): Unit =
    rotateR(-step)

  def reflect(): Unit =
    isReflected = !isReflected

  // using applyO
  def currentHead: A =
    underlying.applyO(headIndex)

   // using startAt and reflectAt
  def current: Seq[A] =
    if (isReflected) underlying.reflectAt(headIndex) else underlying.startAt(headIndex)

}

```

Check the @scaladoc[API documentation for Ring class](io.github.scala_tessella.ring_seq.examples.Ring) for detailed information.
