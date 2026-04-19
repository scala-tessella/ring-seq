# **RingSeq**
[![ring-seq Scala version support](https://index.scala-lang.org/scala-tessella/ring-seq/ring-seq/latest.svg?platform=jvm)](https://index.scala-lang.org/scala-tessella/ring-seq/ring-seq)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.scala-tessella/ring-seq_3.svg?label=Maven%20Central)](https://central.sonatype.com/search?q=ring-seq)
[![Scala.js](https://www.scala-js.org/assets/badges/scalajs-1.16.0.svg)](https://www.scala-js.org)
[![CI](https://github.com/scala-tessella/ring-seq/actions/workflows/ci.yml/badge.svg)](https://github.com/scala-tessella/ring-seq/actions/workflows/ci.yml)
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)

A library that adds new operations to Scala `Seq`
for when a sequence needs to be considered [**circular**](https://scala-tessella.github.io/ring-seq/what-is.html),
its elements forming a ring — the element after the last wraps back to the first.

It works on any immutable or mutable `Seq` and sub-type (`Vector`, `List`, `String`, `ArraySeq`, `ListBuffer`, …),
acting as a _decorator_ via Scala 3 [`extension`](https://docs.scala-lang.org/scala3/book/ca-extension-methods.html)
or Scala 2 [`implicit class`](https://docs.scala-lang.org/overviews/core/implicit-classes.html).

Available for Scala `3.3.7` and `2.13.18`, cross-published for the JVM, Scala.js, and Scala Native.
Zero runtime dependencies.

## Setup

Add the dependency to your `build.sbt`:

```scala
libraryDependencies += "io.github.scala-tessella" %% "ring-seq" % "0.7.0"
// Use %%% instead of %% for Scala.js or Scala Native
```

Then import the [`RingSeq`](src/main/scala-3/io/github/scala_tessella/ring_seq/RingSeq.scala) object — every collection under `Seq` gains the new methods:

```scala
import io.github.scala_tessella.ring_seq.RingSeq.*

// Indexing wraps around
Seq(10, 20, 30).applyO(4)                   // 20

// Rotation produces a new collection of the same type
"RING".rotateRight(1).mkString              // "GRIN"
List(0, 1, 2, 3).startAt(2)                 // List(2, 3, 0, 1)

// Comparison up to rotation
Seq(0, 1, 2).isRotationOf(Seq(2, 0, 1))     // true

// Canonical (necklace) form — for deduplication or hashing
Seq(2, 0, 1).canonical                      // Seq(0, 1, 2)

// Symmetry detection
Seq(0, 1, 0, 1).rotationalSymmetry          // 2
```

## Operations

### [Indexing](https://scala-tessella.github.io/ring-seq/categories/indexing.html)

| Method | Description |
|---|---|
| `indexFrom(i)` | Normalize a circular index to `[0, n)` |
| `applyO(i)` | Element at circular index |

### [Rotation and reflection](https://scala-tessella.github.io/ring-seq/categories/rotation-reflection.html)

| Method | Description |
|---|---|
| `rotateRight(step)` | Rotate right by `step` (negative = left) |
| `rotateLeft(step)` | Rotate left by `step` (negative = right) |
| `startAt(i)` | Rotate so circular index `i` is first |
| `reflectAt(i)` | Reflect so circular index `i` is the axis head |

### [Slicing](https://scala-tessella.github.io/ring-seq/categories/slicing.html)

| Method | Description |
|---|---|
| `sliceO(from, to)` | Circular interval (can exceed ring length) |
| `containsSliceO(that)` | Does the ring contain `that` circularly? |
| `indexOfSliceO(that)` | First circular position of `that` |
| `lastIndexOfSliceO(that)` | Last circular position of `that` |
| `segmentLengthO(p, from)` | Length of prefix satisfying `p` |
| `takeWhileO(p, from)` | Prefix satisfying `p` |
| `dropWhileO(p, from)` | Remainder after that prefix |
| `spanO(p, from)` | `(takeWhileO, dropWhileO)` in one call |

### [Iterators](https://scala-tessella.github.io/ring-seq/categories/iterators.html)

| Method | Description |
|---|---|
| `slidingO(size, step)` | Circular sliding windows |
| `rotations` | All `n` rotations |
| `reflections` | Original + reflection |
| `reversions` | Original + reversal |
| `rotationsAndReflections` | All `2n` variants |
| `groupedO(size)` | Fixed-size circular groups |
| `zipWithIndexO(from)` | Elements paired with their circular indices |

### [Comparisons](https://scala-tessella.github.io/ring-seq/categories/comparisons.html)

| Method | Description |
|---|---|
| `isRotationOf(that)` | Same elements, possibly rotated? |
| `isReflectionOf(that)` | Same elements, possibly reflected? |
| `isReversionOf(that)` | Same elements, possibly reversed? |
| `isRotationOrReflection(that)` | Either of the above? |
| `alignTo(that)` | `Some(k)` with `startAt(k) == that`, or `None` |
| `hammingDistance(that)` | Positional mismatches (same size required) |
| `minRotationalHammingDistance(that)` | Minimum distance over all rotations |

### [Canonical forms](https://scala-tessella.github.io/ring-seq/categories/necklaces.html)

| Method | Description |
|---|---|
| `canonicalIndex` | Index of lex-smallest rotation (Booth's *O(n)*) |
| `canonical` | Lex-smallest rotation (necklace form) |
| `bracelet` | Lex-smallest under rotation *and* reflection |

### [Symmetry](https://scala-tessella.github.io/ring-seq/categories/symmetry.html)

| Method | Description |
|---|---|
| `rotationalSymmetry` | Order of rotational symmetry |
| `symmetryIndices` | Indices on each axis of reflectional symmetry |
| `reflectionalSymmetryAxes` | Full axis geometry as `(AxisLocation, AxisLocation)` pairs (`Vertex` / `Edge`) |
| `symmetry` | Number of reflectional symmetry axes |

## Naming convention

The alternative circular versions of methods that already exist on `Seq` keep the same name
with an appended `O` suffix (meaning _ring_) — for example `applyO` is the circular version of `apply`.
Operations with no standard-library counterpart use plain names.

## Performance notes

Circular operations involve random indexing, which is `O(1)` on `IndexedSeq` (e.g. `Vector`, `ArraySeq`, `String`) and `O(n)` on `LinearSeq` (e.g. `List`).

For best performance on large sequences:
- Prefer `Vector` (or any `IndexedSeq`) over `List`.
- If you start from a `List` and call several circular operations, convert once with `.toVector`.

## Use cases

- **Bioinformatics** — circular DNA/RNA sequence alignment and comparison
- **Graphics** — polygon vertex manipulation, closed curve operations
- **Procedural generation** — tile rings, symmetry-aware pattern generation
- **Music theory** — pitch-class sets, chord inversions
- **Combinatorics** — necklace/bracelet enumeration, Burnside's lemma
- **Embedded / robotics** — circular sensor arrays, rotary encoder positions

## Documentation

- [Website](https://scala-tessella.github.io/ring-seq/) — narrative docs and examples.
- [Usage walkthrough](https://scala-tessella.github.io/ring-seq/usage.html).
- [Scaladoc API](https://scala-tessella.github.io/ring-seq/api/io/github/scala_tessella/ring_seq/RingSeq$.html) — the Scala 3 API reference is valid for Scala 2.13 as well.
- [Changelog](CHANGELOG.md).

## Other languages

The same library, adapted for the specific idiom, is available also for:
- Python — [ring-seq-py](https://github.com/scala-tessella/ring-seq-py)
- Rust — [ring-seq-rs](https://github.com/scala-tessella/ring-seq-rs)

## License

Licensed under either of

- [Apache License, Version 2.0](LICENSE-APACHE)
- [MIT License](LICENSE-MIT)

at your option.
