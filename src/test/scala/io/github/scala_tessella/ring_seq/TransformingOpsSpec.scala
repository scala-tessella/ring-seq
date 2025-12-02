package io.github.scala_tessella.ring_seq

import io.github.scala_tessella.ring_seq.RingSeq._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Prop.forAll
import org.scalacheck.Test.check
import org.scalatest.flatspec._
import org.scalatest.matchers._

import scala.collection.immutable.{Queue, WrappedString}
import scala.collection.mutable
import scala.collection.mutable.{ListBuffer, Queue => MutableQueue}

class TransformingOpsSpec extends AnyFlatSpec with TestHelper with should.Matchers {

  "Any immutable Seq subtype" can "be rotated" in {
    "SCALA".rotateRight(2) shouldEqual WrappedString('L', 'A', 'S', 'C', 'A')
    "SCALA".rotateRight(2).mkString shouldEqual "LASCA"
    val asList   = s12345.toList
    asList.rotateRight(2) shouldBe List(4, 5, 1, 2, 3)
    asList.rotateRight(2) shouldBe a[List[_]]
    asList.rotateRight(2) should not be a[Vector[_]]
    val asVector = s12345.toVector
    asVector.rotateRight(2) shouldBe Vector(4, 5, 1, 2, 3)
    asVector.rotateRight(2) shouldBe a[Vector[_]]
    asVector.rotateRight(2) should not be a[List[_]]
    Queue(1, 2, 3, 4, 5).rotateRight(2) shouldBe Queue(4, 5, 1, 2, 3)
  }

  "Any mutable Seq subtype" can "be rotated" in {
    s12345.toBuffer.rotateRight(2) shouldBe mutable.Buffer(4, 5, 1, 2, 3)
    ListBuffer(1, 2, 3, 4, 5).rotateRight(2) shouldBe ListBuffer(4, 5, 1, 2, 3)
    val sb = new mutable.StringBuilder("ABCDE")
    sb.rotateRight(2).toList shouldBe List('D', 'E', 'A', 'B', 'C')
    MutableQueue(1, 2, 3, 4, 5).rotateRight(2) shouldBe MutableQueue(4, 5, 1, 2, 3)
  }

  "An empty circular sequence" must "remain itself with any possible rotation" in {
    check(
      forAll(arbitrary[Int])(step => e.rotateRight(step) === e)
    )(_)
  }

  "A circular sequence" can "be rotated one step to the right" in {
    "ABCDE".rotateRight(1).mkString shouldBe "EABCD"
  }

  it can "be rotated one step to the left" in {
    "ABCDE".rotateLeft(1).mkString shouldBe "BCDEA"
    "ABCDE".rotateRight(-1).mkString shouldBe "BCDEA"
  }

  it can "be rotated to start where index 1 is" in {
    "ABCDE".startAt(1).mkString shouldBe "BCDEA"
  }

  it can "be reflected" in {
    "ABCDE".reflectAt().mkString shouldBe "AEDCB"
  }

  it can "be reflected at a given index" in {
    "ABCDE".reflectAt(2).mkString shouldBe "CBAED"
  }

  it can "be reflected as reversed" in {
    "ABCDE".reflectAt(-1).mkString shouldBe "ABCDE".reverse
  }

}
