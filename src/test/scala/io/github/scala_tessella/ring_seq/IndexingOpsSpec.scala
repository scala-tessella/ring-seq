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

  "An empty sequence" must "have no normalized index" in {
    assertThrows[ArithmeticException] { Seq.empty.indexFrom(0) }
  }

  "A non-empty circular sequence" must "always have an element indexed before another one" in {
    "ABCDE".applyO(-1) shouldEqual 'E'
  }

  it must "always have an element indexed after another one" in {
    "ABCDE".applyO(5) shouldEqual 'A'
  }

  "While a non-empty sequence" must "have no element indexed before the first" in {
    assertThrows[IndexOutOfBoundsException] { "ABCDE".apply(-1) }
  }

  it must "have no element indexed after the last" in {
    assertThrows[IndexOutOfBoundsException] { "ABCDE".apply(5) }
  }

  "An empty circular sequence" must "have no indexed elements" in {
    assertThrows[ArithmeticException] { Seq.empty.applyO(0) }
  }

  "An empty sequence" must "have no indexed elements" in {
    assertThrows[IndexOutOfBoundsException] { Seq.empty.apply(0) }
  }

  "Any non empty circular sequence" must "return an element for any index" in {
    val elems = "ABC"
    val gen: Gen[(Seq[Char], IndexO)] =
      for {
        seq <- Gen.nonEmptyContainerOf[Seq, Char](Gen.oneOf(elems))
        i <- arbitrary[IndexO]
      } yield (seq, i)
    check(
      forAll(gen)({ case (seq, i) => elems.contains(seq.applyO(i)) })
    )(_)
  }

}