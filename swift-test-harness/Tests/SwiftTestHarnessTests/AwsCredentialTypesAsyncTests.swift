import Testing
import AwsCredentialTypes

@MainActor
@Suite struct AwsCredentialTypesAsyncTests {
    @Test func credentialsFutureRetainsItsValueAcrossAwaitAndPoll() async throws {
        let credentials = Credentials.Companion.shared.fromKeys(
            accessKeyId: "async-key", secretAccessKey: "async-secret", sessionToken: "session")
        let future = credentials.provideCredentials()

        let awaited = try await future.await()
        MainActor.assertIsolated()
        #expect(awaited.isSuccess)
        #expect(!awaited.isFailure)
        #expect(awaited.exceptionOrNull() == nil)
        let first = try #require(awaited.getOrNull() as? Credentials)
        #expect(first.accessKeyId() == "async-key")
        #expect(first.secretAccessKey() == "async-secret")
        #expect(first.sessionToken() == "session")

        let polled = try await future.poll()
        MainActor.assertIsolated()
        #expect(polled.isSuccess)
        let second = try #require(polled.getOrNull() as? Credentials)
        #expect(first === second)
        credentials.zeroize()
        #expect(second.accessKeyId().isEmpty)
        #expect(second.secretAccessKey().isEmpty)
        #expect(second.sessionToken() == nil)
    }

    @Test func tokenFutureRetainsItsValueAcrossAwaitAndPoll() async throws {
        let token = Token.Companion.shared.new(token: "async-token", expiresAfter: nil)
        let future = token.provideToken()

        let awaited = try await future.await()
        MainActor.assertIsolated()
        #expect(awaited.isSuccess)
        #expect(!awaited.isFailure)
        #expect(awaited.exceptionOrNull() == nil)
        let first = try #require(awaited.getOrNull() as? Token)
        #expect(first.token() == "async-token")
        #expect(first.expiration() == nil)

        let polled = try await future.poll()
        MainActor.assertIsolated()
        #expect(polled.isSuccess)
        let second = try #require(polled.getOrNull() as? Token)
        #expect(first === second)
        #expect(token.token() == "async-token")
    }

    @Test func credentialsProviderResolvesIdentityOnCallerActor() async throws {
        let credentials = Credentials.Companion.shared.fromKeys(
            accessKeyId: "identity-key", secretAccessKey: "identity-secret", sessionToken: nil)
        let shared = provider.SharedCredentialsProvider(provider: credentials)
        let original = try #require(shared.asRef() as? Credentials)
        #expect(original === credentials)

        let resolved = try await shared.resolveIdentity()
        MainActor.assertIsolated()
        #expect(resolved.isSuccess)
        #expect(resolved.exceptionOrNull() == nil)
        let identity = try #require(resolved.getOrNull() as? Identity)
        let first = try #require(identity.data as? Credentials)
        let second = try #require(identity.data as? Credentials)
        #expect(first === second)
        #expect(first.accessKeyId() == "identity-key")
        #expect(first.secretAccessKey() == "identity-secret")
        #expect(identity.expiration == nil)
        credentials.zeroize()
        #expect(first.accessKeyId().isEmpty)
        #expect(first.secretAccessKey().isEmpty)
    }

    @Test func tokenProviderResolvesIdentityOnCallerActor() async throws {
        let token = Token.Companion.shared.new(token: "identity-token", expiresAfter: nil)
        let shared = provider.token.SharedTokenProvider(provider: token)
        let original = try #require(shared.asRef() as? Token)
        #expect(original === token)

        let resolved = try await shared.resolveIdentity()
        MainActor.assertIsolated()
        #expect(resolved.isSuccess)
        #expect(resolved.exceptionOrNull() == nil)
        let identity = try #require(resolved.getOrNull() as? Identity)
        let first = try #require(identity.data as? Token)
        let second = try #require(identity.data as? Token)
        #expect(first === second)
        #expect(first.token() == "identity-token")
        #expect(first.expiration() == nil)
        #expect(identity.expiration == nil)
        #expect(token.token() == "identity-token")
    }
}
