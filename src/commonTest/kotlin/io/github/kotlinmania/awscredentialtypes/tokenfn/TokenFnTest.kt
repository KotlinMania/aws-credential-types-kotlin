// port-lint: tests token_fn.rs
package io.github.kotlinmania.awscredentialtypes.tokenfn

import io.github.kotlinmania.awscredentialtypes.Token
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import io.github.kotlinmania.awscredentialtypes.provider.token.Result as TokenResult

private fun testAsyncProvider(input: String): TokenResult =
    Result.success(Token.new(input, null))

suspend fun provideTokenFnClosureCanBorrow() {
    val thingsToBorrow = listOf("one", "two")
    val providers = mutableListOf<ProvideTokenFn>()

    for (thing in thingsToBorrow) {
        val provider =
            provideTokenFn {
                testAsyncProvider(thing)
            }
        providers += provider
    }

    val two = providers.removeLast()
    val one = providers.removeLast()
    assertEquals(
        "one",
        one
            .provideToken()
            .await()
            .getOrThrow()
            .token(),
    )
    assertEquals(
        "two",
        two
            .provideToken()
            .await()
            .getOrThrow()
            .token(),
    )
}

class TokenFnTest {
    // Upstream Send/Sync type bounds and borrowed-string checks have no Kotlin type-system equivalent.
    @Test
    fun provideTokenFnClosureCanBorrow() =
        runTest {
            io.github.kotlinmania.awscredentialtypes.tokenfn
                .provideTokenFnClosureCanBorrow()
        }
}
