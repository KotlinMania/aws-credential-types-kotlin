import Testing
import AwsCredentialTypes

// Smoke test for the Kotlin → Swift Export → SPM → swift test pipeline.
@Suite struct AwsCredentialTypesExportTests {
    @Test func testSwiftModuleLoads() throws {
        #expect(true, "AwsCredentialTypes swift module imported cleanly")
    }
}
