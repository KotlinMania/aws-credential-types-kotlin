// port-lint: source src/provider/credentials.rs
package io.github.kotlinmania.awscredentialtypes.provider

import io.github.kotlinmania.awscredentialtypes.Credentials
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CredentialsProviderTest {
    @Test
    fun sharedCredentialsProviderForwardsToInner() = runTest {
        val creds = Credentials.create("AKID", "SECRET", null, null, "test")
        val provider = SharedCredentialsProvider(creds)
        val resolved = provider.provideCredentials().await()
        assertTrue(resolved.isSuccess)
        assertEquals("AKID", resolved.getOrThrow().accessKeyId())
        assertEquals("SECRET", resolved.getOrThrow().secretAccessKey())
        assertEquals("test", resolved.getOrThrow().toString().let { it.substringAfter("provider_name: \"").substringBefore("\"") })
    }

    @Test
    fun credentialsImplementsProvideCredentialsDirectly() = runTest {
        val creds = Credentials.create("AKID2", "SECRET2", "session", null, "direct")
        val resolved = creds.provideCredentials().await()
        assertEquals("AKID2", resolved.getOrThrow().accessKeyId())
        assertEquals("session", resolved.getOrThrow().sessionToken())
    }
}
