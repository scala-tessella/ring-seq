# Runbook

## OpenJDK JHM Benchmarks

### How to run

#### Full suite, default settings
`sbt 'benchmarks/Jmh/run -f1'`

#### Filter to one class
`sbt 'benchmarks/Jmh/run -f1 .*SymmetryBench.*'`

#### Quick sanity (1 warmup, 1 measurement, ~30s total)
`sbt 'benchmarks/Jmh/run -wi 1 -i 1 -f1 .*SymmetryBench.*'
`
#### Save a baseline JSON for before/after comparison
`sbt 'benchmarks/Jmh/run -f1 -rf json -rff baseline.json'                                                                      
`