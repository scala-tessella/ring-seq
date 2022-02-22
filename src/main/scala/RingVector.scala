trait RingVector {

  /* for improved readability, a Vector index */
  type Index = Int

  /* and a RingVector index, any value is valid */
  type IndexO = Int

  implicit class Ringed[A](ring: Vector[A]) {

    private def index(i: IndexO): Index =
      java.lang.Math.floorMod(i, ring.size)

    def applyO(i: IndexO): A =
      ring(index(i))

    def rotateRight(step: Int): Vector[A] =
      if (ring.isEmpty) ring
      else {
        val j: Index = ring.size - index(step)
        ring.drop(j) ++ ring.take(j)
      }

    def rotateLeft(step: Int): Vector[A] =
      rotateRight(-step)

    def startAt(i: IndexO): Vector[A] =
      rotateLeft(i)

    def reflectAt(i: IndexO = 0): Vector[A] =
      startAt(i + 1).reverse

    def segmentLengthO(p: A => Boolean, from: IndexO = 0): Int =
      startAt(from).segmentLength(p)

    protected def multiply(times: Int): Vector[A] =
      Vector.fill(times)(ring).flatten

    def sliceO(from: IndexO, to: IndexO): Vector[A] =
      if (from >= to || ring.isEmpty) Vector.empty
      else {
        val length = to - from
        val times = Math.ceil(length / ring.size).toInt + 1
        startAt(from).multiply(times).take(length)
      }

    private def growBy(growth: Int): Vector[A] =
      sliceO(0, ring.size + growth)

    def containsSliceO(slice: Vector[A]): Boolean =
      growBy(slice.size - 1).containsSlice(slice)

    def indexOfSliceO(slice: Vector[A]): Index =
      growBy(slice.size - 1).indexOfSlice(slice)

    def lastIndexOfSliceO(slice: Vector[A]): Index =
      growBy(slice.size - 1).lastIndexOfSlice(slice)

    def lastIndexOfSliceO(slice: Vector[A], end: Index): Index =
      growBy(slice.size - 1).lastIndexOfSlice(slice, end)

    def slidingO(size: Int, step: Int = 1): Iterator[Vector[A]] =
      sliceO(0, step * (ring.size - 1) + size).sliding(size, step)

    private def transformations(f: Vector[A] => Iterator[Vector[A]]): Iterator[Vector[A]] =
      if (ring.isEmpty) Iterator(ring) else f(ring)

    def rotations: Iterator[Vector[A]] =
      transformations(r => slidingO(r.size))

    def reflections: Iterator[Vector[A]] =
      transformations(r => List(r, r.reflectAt()).iterator)

    def reversions: Iterator[Vector[A]] =
      transformations(r => List(r, r.reverse).iterator)

    def rotationsAndReflections: Iterator[Vector[A]] =
      transformations(r => r.rotations ++ r.reverse.rotations)

    def minRotation(implicit ordering: Ordering[Vector[A]]): Vector[A] =
      rotations.min(ordering)

    private def isTransformationOf(other: Vector[A], f: Vector[A] => Iterator[Vector[A]]): Boolean =
      ring.sizeCompare(other) == 0 && f(ring).contains(other)

    def isRotationOf(other: Vector[A]): Boolean =
      isTransformationOf(other, _.rotations)

    def isReflectionOf(other: Vector[A]): Boolean =
      isTransformationOf(other, _.reflections)

    def isRotationOrReflectionOf(other: Vector[A]): Boolean =
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

    private def greaterHalfSize: Int =
      Math.ceil(ring.size / 2.0).toInt

    private def checkReflectionAxis(gap: Int): Boolean =
      (0 until greaterHalfSize).forall(j => applyO(j + 1) == applyO(-(j + gap)))

    protected def hasHeadOnAxis: Boolean =
      checkReflectionAxis(1)

    protected def hasAxisBetweenHeadAndNext: Boolean =
      checkReflectionAxis(0)

    protected def findReflectionSymmetry: Option[Index] =
      (0 until greaterHalfSize).find(j => {
        val rotation = startAt(j)
        rotation.hasHeadOnAxis || rotation.hasAxisBetweenHeadAndNext
      })

    def symmetryIndices: List[Index] =
      if (ring.isEmpty) Nil
      else {
        val folds = rotationalSymmetry
        val foldSize = ring.size / folds
        ring.take(foldSize).findReflectionSymmetry match {
          case None => Nil
          case Some(j) => (0 until folds).toList.map(_ * foldSize + j)
        }
      }

    def symmetry: Int =
      symmetryIndices.size

  }

}