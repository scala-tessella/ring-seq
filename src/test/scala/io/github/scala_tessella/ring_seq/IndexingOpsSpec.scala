package io.github.scala_tessella.ring_seq

import io.github.scala_tessella.ring_seq.RingSeq._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import org.scalacheck.Test.check
import org.scalatest.flatspec._
import org.scalatest.matchers._

import scala.collection.Seq

class IndexingOpsSpec extends AnyFlatSpec with TestHelper with should.Matchers {

  "A non-empty circular sequence" must "have a circular index before 0 normalized" in {
    "ABCDE".indexFrom(-1) shouldEqual 4
  }

  it must "have a circular index after the last element normalized" in {
    "ABCDE".indexFrom(5) shouldEqual 0
  }

  "An empty sequence" must "have no normalized index" in
    assertThrows[ArithmeticException](Seq.empty.indexFrom(0))

  "A non-empty circular sequence" must "always have an element indexed before another one" in {
    "ABCDE".applyO(-1) shouldEqual 'E'
  }

  it must "always have an element indexed after another one" in {
    "ABCDE".applyO(5) shouldEqual 'A'
  }

  "An empty circular sequence" must "have no indexed elements" in
    assertThrows[ArithmeticException](Seq.empty.applyO(0))

  "An empty sequence" must "have no indexed elements" in
    assertThrows[IndexOutOfBoundsException](Seq.empty.apply(0))

  "A non-empty circular sequence" must "optionally return an element at any circular index" in {
    "ABCDE".liftO(-1) shouldEqual Some('E')
    "ABCDE".liftO(5) shouldEqual Some('A')
  }

  "An empty circular sequence" must "optionally return no element" in {
    Seq.empty[Int].liftO(0) shouldEqual None
  }

  "A non-empty circular sequence" must "find the circular index of an element" in {
    "ABCDE".indexOfO('A') shouldEqual 0
    "ABCDE".indexOfO('E') shouldEqual 4
  }

  it must "find an element circularly, wrapping past the end" in {
    "ABCDE".indexOfO('A', 1) shouldEqual 0
    "ABCDE".indexOfO('C', -2) shouldEqual 2
  }

  it must "not find an element that is absent" in {
    "ABCDE".indexOfO('X') shouldEqual -1
    "ABCDE".indexOfO('X', 3) shouldEqual -1
  }

  "An empty circular sequence" must "find no element" in {
    Seq.empty[Int].indexOfO(0) shouldEqual -1
  }

  "Any non empty circular sequence" must "return an element for any index" in {
    val elems                         = "ABC"
    val gen: Gen[(Seq[Char], IndexO)] =
      for {
        seq <- Gen.nonEmptyContainerOf[Seq, Char](Gen.oneOf(elems))
        i   <- arbitrary[IndexO]
      } yield (seq, i)
    check(
      forAll(gen) { case (seq, i) =>
        elems.contains(seq.applyO(i))
      }
    )(_)
  }

}
