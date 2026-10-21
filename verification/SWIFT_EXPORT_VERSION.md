# Swift exporter version

## Published tooling selection

The production exporter selects
`io.github.kotlinmania:swift-export-embeddable:2.4.20-km.1`, independently
retrieved from Maven Central after the tooling release's PR and main CI.
KGP and Kotlin/Native remain 2.4.20. `swift-tooling-lock.json` records the
published worker closure, helper and official SDK digests.

`./gradlew swiftExportSmokeTest --no-daemon --max-workers=2 --no-configuration-cache`
resolves the exporter and SDK helper through Gradle, verifies all worker
hashes, and runs the published helper against a verified official SDK in
`build/swift-tooling`. Only its separate `native-sdk` copy is selected for
the nested Native export. Native auto-caches use `build/native-auto-cache`,
and the complete prepared SDK inventory is checked after the tests.
Installed compilers and SDKs are never modified.

Generated dependencies retain their producer manifest and Swift 5 language
mode; the authored test package explicitly uses Swift 6. Both Swift and
Native compilation retain warnings as errors. The task checks the six
AWS behavior cases and actual frontend modes separately from package
configuration and generated test-runner commands. No generated Swift API
is rewritten. The mutable `KotlinError.wrapped` contract is preserved.
