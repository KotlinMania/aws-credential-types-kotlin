import Testing
import AwsCredentialTypes

@Suite struct AwsCredentialTypesExportTests {
    @Test func credentialsRoundTripAndZeroize() {
        let credentials = Credentials.Companion.shared.fromKeys(
            accessKeyId: "example-key", secretAccessKey: "example-secret", sessionToken: "session")
        #expect(credentials.accessKeyId() == "example-key")
        #expect(credentials.secretAccessKey() == "example-secret")
        #expect(credentials.sessionToken() == "session")
        let clone = credentials.clone()
        #expect(clone.accessKeyId() == "example-key")
        credentials.zeroize()
        #expect(clone.accessKeyId().isEmpty)
        #expect(clone.secretAccessKey().isEmpty)
        #expect(clone.sessionToken() == nil)
    }

    @Test func tokenRoundTripAndRedaction() {
        let token = Token.Companion.shared.new(token: "private-token", expiresAfter: nil)
        #expect(token.token() == "private-token")
        #expect(token.expiration() == nil)
        #expect(token.clone().token() == "private-token")
        #expect(!token.toString().contains("private-token"))
    }
}
