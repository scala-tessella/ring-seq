package io.github.scala_tessella.ring_seq

import org.scalatest.flatspec._
import org.scalatest.matchers._

class IndexingOpsExceptionSpec extends AnyFlatSpec with TestHelper with should.Matchers {

  "While a non-empty sequence" must "have no element indexed before the first" in {
    assertThrows[IndexOutOfBoundsException] { "ABCDE".apply(-1) }
  }

  it must "have no element indexed after the last" in {
    assertThrows[IndexOutOfBoundsException] { "ABCDE".apply(5) }
  }

}