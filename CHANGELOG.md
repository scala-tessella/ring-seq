# Changelog

All notable changes to this project are documented here.
The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.9.0] - 2026-07-05

### Added

- **`RingView` — a lazy view layer.** `seq.ring` (also available on `String` and `StringBuilder`) wraps any `Seq` in a `RingView`: a rotation offset and a reflection flag over the same elements, with a single internal index translation. `rotateRight` / `rotateLeft` / `startAt` / `reflectAt` / `reverse` are O(1) and return views; `rotations` / `reflections` / `reversions` / `rotationsAndReflections` yield views in O(1) each, so searches short-circuit without materializing. On the view the circular operations carry their plain names (no `O` suffix): wrapping `apply`, `lift`, `indexOf`, `slice`, `sliding`, `grouped`, `zipWithIndex`, and all comparison, necklace and symmetry operations. Materialize with `toSeq` / `toVector` / `to(factory)` / `iterator`. Mirrors the `Circular` view design of ring-seq-rs 0.3 (its ADR 0001); the source is shared between Scala 2.13 and Scala 3.
- Exhaustive all-views cross-check in `ExhaustiveSmallRingSpec`: every `RingView` operation is compared against its eager counterpart under every reachable `(offset, reflected)` view of the small-ring corpus.
- **`liftO(i)`** — the circular version of `lift`: `Some(element)` at any circular index, `None` only for an empty sequence. Fills the gap with the `get` accessors of ring-seq-rs and ring-seq-py.
- **`indexOfO(elem, from)`** — the circular version of `indexOf`: the index of the first occurrence of an element, searching circularly from a circular index and wrapping past the end.
- **`ExhaustiveSmallRingSpec`** — exhaustive reference test in the spirit of ring-seq-rs `tests/algebra.rs`: all 217 rings of length ≤ 4 over `{0, 1, 2}` and length 5–6 over `{0, 1}` are checked against naive, independently derived reference implementations of the symmetry and necklace operations (including the axis geometry of `reflectionalSymmetryAxes`, previously the least-tested method).
- Scaladoc `@example` for `reflectionalSymmetryAxes`, now also asserted by `ScaladocExampleSpec`.

### Changed

- **Breaking (Scala 2.13, trait implementors only)** — the decorator traits' abstract member `ring: CC[A]` is renamed `underlying`, freeing the `ring` name for the view entry point. Code that merely does `import RingSeq._` is unaffected; only custom classes mixing the decorator traits in must rename the overridden member. `RingStringEnrichment.ring` and `RingStringBuilderEnrichment.ring` now return a `RingView[Char]` instead of `Seq[Char]` (the plain conversion is still available as `underlying`).
- **`canonicalIndex` / `canonical` / `bracelet` now use the two-pointer minimal-rotation algorithm** instead of Booth's: same O(n) time and identical results, but O(1) extra space (no failure-array allocation). This aligns the implementation with ring-seq-rs 0.3.1 and ring-seq-py.
- Scala 3 `sliceO` / `containsSliceO` parameter names aligned with the Scala 2.13 tree and the standard library (`until`, `that`); scaladoc `@param` tags now match the actual parameters.
- `docs/runbook.md` renamed to `docs/benchmarks.md` — it documents the JMH benchmarks; the release runbook remains [`.github/RUNBOOK.md`](.github/RUNBOOK.md).

### Fixed

- **`indexOfSliceO` / `lastIndexOfSliceO` edge cases.** Searching an empty sequence threw `ArithmeticException` instead of returning -1; searching for an empty slice could throw on small sequences. Now: a non-empty slice on an empty sequence yields -1; an empty slice is found at the normalized `from`/`end` index (0 on an empty sequence), aligning with standard `indexOfSlice` / `lastIndexOfSlice`. `RingView.indexOfSlice` / `lastIndexOfSlice` inherit the fix.
- README drift: the setup snippet now points to the latest release (was still `0.7.0`), the Scala.js badge shows `1.21.0` (was `1.16.0`), and the comparisons table lists `isRotationOrReflectionOf` under its real name.
- Stale header comments in `ComparingBench` and `SlicingBench` that still described the pre-0.8.0 implementations (`minRotationalHammingDistance` allocating all rotations, `sliceO` folding with `++`).
- `.gitignore` now covers sbt/IDE build directories (`target/`, `.bsp/`, `.idea/`, …) and local JMH result snapshots (`benchmarks/*.json`).

## [0.8.0] - 2026-04-20

### Added

- `RingSeq` now re-exports the axis-location types, so `import RingSeq._` (Scala 2.13) / `import RingSeq.*` (Scala 3) is enough to bring both the extension methods and the `AxisLocation` / `Vertex` / `Edge` types into scope — no more second import from `SymmetryOps`. Achieved via `export` in Scala 3 and via `type` + companion-`val` aliases in Scala 2.13.
- JMH benchmark `ComparingBench.minRotationalHammingDistance_{rotation,oneOff,reversed}` at sizes 16/256/4096, covering the min/mid/max regimes of the pruning heuristic. Exercises with `sbt "benchmarks/Jmh/run -prof gc -- ComparingBench.minRotationalHammingDistance"`.

### Performance

- **`minRotationalHammingDistance` rewritten to avoid allocating rotations.** The old implementation did `ring.rotations.map(hammingOf(_, that)).min`, which allocated `n` intermediate `CC[A]` instances. The new implementation materialises the two sequences once to `IndexedSeq`, then compares by index with a rotating offset and a running-best prune. Semantics are unchanged — same 15 `ComparingOpsSpec` tests pass on both Scala 3 and 2.13.

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
