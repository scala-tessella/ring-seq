import Ordering.Implicits.*

trait RingVector:

  type Index = Int
  type IndexO = Int

  extension[A](ring: Vector[A])
  
    private def index(i: IndexO): Index =
      java.lang.Math.floorMod(i, ring.size)

    def applyO(i: IndexO): A =
      ring(index(i))

    def rotateRight(step: Int): Vector[A] = 
      if ring.isEmpty then ring
      else
        val j: Index = ring.size - index(step) 
        ring.drop(j) ++ ring.take(j)

    def rotateLeft(step: Int): Vector[A] =
      rotateRight(-i)

    def startAt(i: IndexO): Vector[A] =
      rotateLeft(i)

    def reflectAt(i: IndexO = 0): Vector[A] =
      startAt(i + 1).reverse

    def segmentLengthO(p: A => Boolean, from: IndexO = 0): Int =
      startAt(from).segmentLength(p)

    private def multiply(times: Int): Vector[A] =
      List.fill(times)(ring).toVector.flatten

    def sliceO(from: IndexO, to: IndexO): Vector[A] =
      if from >= to || ring.isEmpty then ring
      else
        val length = to - from
        val times = Math.ceil(length / ring.size).toInt + 1
        startAt(from).multiply(times).take(length)
 
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

    def allRotations: Iterator[Vector[A]] =
      slidingO(ring.size)

    def minRotation(implicit ordering: Ordering[Vector[A]]): Vector[A] =
      allRotations.min(ordering)

    def isRotationOf(other: Vector[A]): Boolean =
      allRotations.exists(_ == other)

    def isReflectionOf(other: Vector[A]): Boolean =
      ring.reflectAt() == other

    def isRotationOrReflectionOf(other: Vector[A]): Boolean =
      val reflected = other.reverse
      allRotations.exists(r => r == other || r == reflected)

    private def areFoldsSymmetrical: Int => Boolean =
      n => rotateRight(ring.size / n) == ring

    def rotationalSymmetry: Int =
      val size = ring.size
      val exactFoldsDesc = size +: (size / 2 to 2 by -1).filter(size % _ == 0)
      exactFoldsDesc.find(areFoldsSymmetrical).getOrElse(1)

    private def greaterHalfSize: Int =
      Math.ceil(ring.size / 2.0).toInt

    private def checkReflectionAxis(gap: Int): Boolean =
      (0 until greaterHalfSize).forall(j => applyO(j + 1) == applyO(-(j + gap)))

    private def hasHeadOnAxis: Boolean =
      checkReflectionAxis(1)

    private def hasAxisBetweenHeadAndNext: Boolean =
      checkReflectionAxis(0)

    private def findReflectionSymmetry: Option[Index] =
      (0 until greaterHalfSize).find(j =>
        val rotation = startAt(j)
        rotation.hasHeadOnAxis || rotation.hasAxisBetweenHeadAndNext
      )
   
    def symmetryIndices: List[Index] =
      val folds = rotationalSymmetry
      val foldSize = ring.size / folds
      ring.take(foldSize).findReflectionSymmetry match
        case None => Nil
        case Some(j) => (0 until folds).toList.map(_ * foldSize + j)

    def symmetry: Int =
      symmetryIndices.size
