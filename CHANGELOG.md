# Changelog

All notable changes to this project are documented here.
The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.8.0] - Unreleased

### Changed

- **Breaking — `groupedO` semantics.** `groupedO(size)` now partitions the ring into `ceil(n / size)` **non-overlapping** blocks (the last wrapping across the seam so every block has exactly `size` elements), aligning with the intuition of standard `grouped`. Previously it was `slidingO(size, size)`, which produced `n` strided sliding windows that covered every ring position rather than partitioning. Callers that relied on the old behaviour should switch to `slidingO(size, size)` explicitly.
  ```scala
  // old: Seq(0, 1, 2, 3, 4).groupedO(2) ==
  //      Iterator(Seq(0, 1), Seq(2, 3), Seq(4, 0), Seq(1, 2), Seq(3, 4))
  // new: Seq(0, 1, 2, 3, 4).groupedO(2) ==
  //      Iterator(Seq(0, 1), Seq(2, 3), Seq(4, 0))
  ```

## [0.7.1] - 2026-04-19

### Changed

- **Dual-licensed under Apache-2.0 OR MIT.** Previously Apache-2.0 only. Downstream users may now choose either license at their option; no rights are removed from anyone already consuming the library under Apache-2.0. The repository now ships `LICENSE-APACHE` (the former `LICENSE`, renamed) and `LICENSE-MIT`.
- `build.sbt` `licenses` field now lists both entries using SPDX identifiers (`Apache-2.0`, `MIT`) instead of the non-standard `APL2` shorthand.

## [0.7.0] - 2026-04-19

### Added

- **Scala Native support.** `ring-seq` is now cross-published for JVM, Scala.js, and Scala Native (`%%%` continues to work for consumers).
- **`NecklaceOps`** — a new module exposing canonical-form operations for circular sequences, implemented with Booth's O(n) algorithm:
  - `canonicalIndex` — starting index of the lexicographically smallest rotation.
  - `canonical` — the lexicographically smallest rotation (necklace canonical form).
  - `bracelet` — the lexicographically smallest representative under both rotation and reflection.
- **`ComparingOps`** — new methods:
  - `alignTo(that)` — rotation offset that aligns this ring to `that`, or `None` if the two are not rotations of each other.
  - `hammingDistance(that)` — elementwise mismatch count between two rings of equal size.
  - `minRotationalHammingDistance(that)` — the minimum Hamming distance over all rotations.
- **`IteratingOps`** — new methods:
  - `groupedO(size)` — iterator of consecutive circular slices.
  - `zipWithIndexO(from)` — iterator of `(element, index)` pairs starting at a circular index.
- **`SlicingOps`** — new circular counterparts to standard `Seq` combinators: `takeWhileO`, `dropWhileO`, `spanO`.
- **`SymmetryOps.reflectionalSymmetryAxes`** — returns the pairs of `AxisLocation` points (vertices or edges) each symmetry axis passes through.
- **JMH benchmarks module** (`benchmarks/`) for tracking performance of ring operations over time.

### Changed

- **Breaking — `Edge` construction.** `SymmetryOps.Edge` is now a `sealed abstract case class` that enforces the invariant `j == (i + 1) mod n`. Construct with `Edge(i, n)` (the factory computes `j`). Direct construction via `Edge(i, j)` is no longer possible; pattern matching `case Edge(i, j) => …` continues to work.
- **Breaking — `AxisLocation` location.** `AxisLocation`, `Vertex`, and `Edge` moved from inside `trait SymmetryOps` to the `object SymmetryOps` companion. Consumers using them via the enrichment import are unaffected; code that referenced them under a different path must update its imports.
- **Breaking — `symmetryIndices` return type.** `List[Int]` → `List[Index]`. `Index` is a type alias, so binary compatibility is preserved but source that pattern-matched the raw type may need a tweak.
- **Performance — `rotationalSymmetry` and `symmetryIndices`** now materialize the sequence once and compare by index, avoiding per-candidate rotation allocation. Substantial speedup for non-`IndexedSeq` inputs and for long rings.
- Scala 3 bumped to `3.3.7` (was `3.3.3`); Scala 2.13 bumped to `2.13.18` (was `2.13.13`).
- sbt bumped to `1.12.9`.
- Dependency refresh: ScalaTest `3.2.19`, ScalaCheck `1.19.0`, sbt-scalajs `1.20.1`, Scala Native `0.5.9`.

### Fixed

- **`rotationalSymmetry`** — the internal rotation comparison used `rotateRight` where `rotateLeft` was required; result was correct for most inputs but returned an incorrect order on some asymmetric sequences. Now uses `rotateLeft`.
- **`reflectionalSymmetryAxes`** — initial implementation returned incorrect axes for some configurations; algorithm corrected to map the reflection `i → (n - 1 - shift - i) mod n` and split cases on parity of `n` and `K = (n - 1 - shift) mod n`.

### Infrastructure

- Publishing migrated from the sunset OSSRH endpoint to the **Sonatype Central Portal** via `sbt-ci-release`; releases are triggered by pushing a `v*` git tag. See [`.github/RUNBOOK.md`](.github/RUNBOOK.md).
- CI now builds and tests Scala Native on every push (in addition to JVM and Scala.js).
- `scalafmt` added to the project; all sources formatted.
- GitHub Actions workflows bumped to `actions/checkout@v5` and `actions/setup-java@v5` (Node 24).

## [0.6.2] - 2024-04-20

Last release before this changelog. See the [v0.6.2 tag](https://github.com/scala-tessella/ring-seq/releases/tag/v0.6.2).
