package io.github.kotlinmania.awscredentialtypes.provider

import io.github.kotlinmania.awscredentialtypes.Credentials
import io.github.kotlinmania.awscredentialtypes.Token
import io.github.kotlinmania.awscredentialtypes.credentialfn.provideCredentialsFn
import io.github.kotlinmania.awscredentialtypes.provider.error.CredentialsError
import io.github.kotlinmania.awscredentialtypes.provider.error.TokenError
import io.github.kotlinmania.awscredentialtypes.provider.token.SharedTokenProvider
import io.github.kotlinmania.awscredentialtypes.provider.token.TokenIdentityResult
import io.github.kotlinmania.awscredentialtypes.provider.token.TokenResult
import io.github.kotlinmania.awscredentialtypes.tokenfn.provideTokenFn
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue
import io.github.kotlinmania.awscredentialtypes.provider.future.ProvideCredentials as CredentialsFuture
import io.github.kotlinmania.awscredentialtypes.provider.future.ProvideToken as TokenFuture

private class DiagnosticFailure(
    val code: Int,
    cause: Throwable? = null,
) : Throwable("provider failure", cause)

class ProviderResultTest {
    @Test
    fun credentialsSuccessRetainsPayload() {
        val credentials = Credentials.fromKeys("key", "secret", null)
        val result = CredentialsResult.success(credentials)
        assertTrue(result.isSuccess)
        assertFalse(result.isFailure)
        assertSame(credentials, result.value)
        assertSame(credentials, result.getOrNull())
        assertSame(credentials, result.getOrThrow())
        assertNull(result.error)
        assertNull(result.exceptionOrNull())
    }

    @Test
    fun tokenSuccessRetainsPayload() {
        val token = Token.new("token", null)
        val result = TokenResult.success(token)
        assertTrue(result.isSuccess)
        assertFalse(result.isFailure)
        assertSame(token, result.value)
        assertSame(token, result.getOrNull())
        assertSame(token, result.getOrThrow())
        assertNull(result.error)
        assertNull(result.exceptionOrNull())
    }

    @Test
    fun credentialsFailureRetainsErrorAndSourceAcrossFuture() =
        runTest {
            val root = DiagnosticFailure(41)
            val source = DiagnosticFailure(42, root)
            val error = CredentialsError.providerError(source)
            val result = CredentialsResult.failure(error)
            assertFalse(result.isSuccess)
            assertTrue(result.isFailure)
            assertNull(result.value)
            assertNull(result.getOrNull())
            assertSame(error, result.error)
            assertSame(error, result.exceptionOrNull())
            assertSame(error, assertFails { result.getOrThrow() })
            assertSame(source, error.source())
            assertEquals(42, assertIs<DiagnosticFailure>(error.source()).code)
            assertSame(root, source.cause)
            val future = CredentialsFuture.ready(result)
            assertSame(result, future.await())
            assertSame(result, future.poll())
        }

    @Test
    fun tokenFailureRetainsErrorAndSourceAcrossFuture() =
        runTest {
            val root = DiagnosticFailure(51)
            val source = DiagnosticFailure(52, root)
            val error = TokenError.invalidConfiguration(source)
            val result = TokenResult.failure(error)
            assertFalse(result.isSuccess)
            assertTrue(result.isFailure)
            assertNull(result.value)
            assertNull(result.getOrNull())
            assertSame(error, result.error)
            assertSame(error, result.exceptionOrNull())
            assertSame(error, assertFails { result.getOrThrow() })
            assertSame(source, error.source())
            assertEquals(52, assertIs<DiagnosticFailure>(error.source()).code)
            assertSame(root, source.cause)
            val future = TokenFuture.ready(result)
            assertSame(result, future.await())
            assertSame(result, future.poll())
        }

    @Test
    fun credentialsIdentitySuccessRetainsIdentityAndPayload() =
        runTest {
            val credentials = Credentials.fromKeys("key", "secret", null)
            val identity = credentials.toIdentity()
            val result = CredentialsIdentityResult.success(identity)
            assertTrue(result.isSuccess)
            assertFalse(result.isFailure)
            assertSame(identity, result.value)
            assertSame(identity, result.getOrNull())
            assertSame(identity, result.getOrThrow())
            assertNull(result.error)
            assertNull(result.exceptionOrNull())
            val provider = SharedCredentialsProvider(provideCredentialsFn { CredentialsResult.success(credentials) })
            val resolved = provider.resolveIdentity().getOrThrow()
            assertSame(credentials, resolved.data)
        }

    @Test
    fun tokenIdentitySuccessRetainsIdentityAndPayload() =
        runTest {
            val token = Token.new("token", null)
            val identity = token.toIdentity()
            val result = TokenIdentityResult.success(identity)
            assertTrue(result.isSuccess)
            assertFalse(result.isFailure)
            assertSame(identity, result.value)
            assertSame(identity, result.getOrNull())
            assertSame(identity, result.getOrThrow())
            assertNull(result.error)
            assertNull(result.exceptionOrNull())
            val provider = SharedTokenProvider(provideTokenFn { TokenResult.success(token) })
            val resolved = provider.resolveIdentity().getOrThrow()
            assertSame(token, resolved.data)
        }

    @Test
    fun credentialsIdentityFailureRetainsDomainError() =
        runTest {
            val source = DiagnosticFailure(61)
            val error = CredentialsError.notLoaded(source)
            val provider = SharedCredentialsProvider(provideCredentialsFn { CredentialsResult.failure(error) })
            val result = provider.resolveIdentity()
            assertFalse(result.isSuccess)
            assertTrue(result.isFailure)
            assertNull(result.value)
            assertNull(result.getOrNull())
            assertSame(error, result.error)
            assertSame(error, result.exceptionOrNull())
            assertSame(error, assertFails { result.getOrThrow() })
            assertIs<CredentialsError.CredentialsNotLoaded>(result.error)
            assertSame(source, requireNotNull(result.error).source())
        }

    @Test
    fun tokenIdentityFailureRetainsDomainError() =
        runTest {
            val source = DiagnosticFailure(71)
            val error = TokenError.unhandled(source)
            val provider = SharedTokenProvider(provideTokenFn { TokenResult.failure(error) })
            val result = provider.resolveIdentity()
            assertFalse(result.isSuccess)
            assertTrue(result.isFailure)
            assertNull(result.value)
            assertNull(result.getOrNull())
            assertSame(error, result.error)
            assertSame(error, result.exceptionOrNull())
            assertSame(error, assertFails { result.getOrThrow() })
            assertIs<TokenError.Unhandled>(result.error)
            assertSame(source, requireNotNull(result.error).source())
        }
}
