// port-lint: tests provider/credentials.rs
package io.github.kotlinmania.awscredentialtypes.provider

import io.github.kotlinmania.awscredentialtypes.Credentials
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CredentialsProviderTest {
    @Test
    fun sharedCredentialsProviderForwardsToInner() =
        runTest {
            val creds = Credentials.create("AKID", "SECRET", null, null, "test")
            val provider = SharedCredentialsProvider(creds)
            val resolved = provider.provideCredentials().await()
            assertTrue(resolved.isSuccess)
            assertEquals("AKID", resolved.getOrThrow().accessKeyId())
            assertEquals("SECRET", resolved.getOrThrow().secretAccessKey())
            assertEquals("test", resolved.getOrThrow().toString().let { it.substringAfter("provider_name: \"").substringBefore("\"") })
        }

    @Test
    fun credentialsImplementsProvideCredentialsDirectly() =
        runTest {
            val creds = Credentials.create("AKID2", "SECRET2", "session", null, "direct")
            val resolved = creds.provideCredentials().await()
            assertEquals("AKID2", resolved.getOrThrow().accessKeyId())
            assertEquals("session", resolved.getOrThrow().sessionToken())
        }

    @Test
    fun reusesCachePartition() {
        val creds = Credentials.new("AKID", "SECRET", null, null, "test")
        val provider = SharedCredentialsProvider.new(creds)
        val partition = provider.cachePartition()
        assertNotNull(partition)
        assertEquals(partition, provider.cachePartition())
    }

    @Test
    fun accountIdCanBeRetrievedFromIdentity() =
        runTest {
            val expectedAccountId = "012345678901"
            val creds =
                Credentials.builder()
                    .accessKeyId("AKID")
                    .secretAccessKey("SECRET")
                    .accountId(expectedAccountId)
                    .providerName("test")
                    .build()
            val provider = SharedCredentialsProvider.new(creds)
            val identity = provider.provideCredentials().await().getOrThrow()
            val actual = identity.accountId()
            assertNotNull(actual)
            assertEquals(expectedAccountId, actual.asStr())
        }
}
