// port-lint: source provider/token.rs
package io.github.kotlinmania.awscredentialtypes.provider

import io.github.kotlinmania.awscredentialtypes.Token
import io.github.kotlinmania.awscredentialtypes.provider.token.SharedTokenProvider
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TokenProviderTest {
    @Test
    fun tokenImplementsProvideTokenDirectly() =
        runTest {
            val token = Token.new("token", null)
            val resolved = token.provideToken().await().getOrThrow()
            assertEquals("token", resolved.token())
        }

    @Test
    fun sharedTokenProviderForwardsToInner() =
        runTest {
            val token = Token.new("token", null)
            val provider = SharedTokenProvider.new(token)
            val resolved = provider.provideToken().await().getOrThrow()
            assertEquals("token", resolved.token())
        }

    @Test
    fun reusesCachePartition() {
        val provider = SharedTokenProvider.new(Token.new("token", null))
        assertNotNull(provider.cachePartition())
        assertEquals(provider.cachePartition(), provider.cachePartition())
    }
}
