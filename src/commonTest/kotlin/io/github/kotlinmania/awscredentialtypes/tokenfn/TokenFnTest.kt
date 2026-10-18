// port-lint: tests token_fn.rs
package io.github.kotlinmania.awscredentialtypes.tokenfn

import io.github.kotlinmania.awscredentialtypes.Token
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import io.github.kotlinmania.awscredentialtypes.provider.token.Result as TokenResult

private fun <T> assertSendSync() {}

private fun checkIsStrRef(input: String) {
    assertEquals(input, input)
}

private fun testAsyncProvider(input: String): TokenResult =
    Result.success(Token.new(input, null))

fun credsAreSendSync() {
    assertSendSync<Token>()
    val token = Token.new("token", null)
    assertEquals("token", token.token())
}

suspend fun provideTokenFnClosureCanBorrow() {
    val thingsToBorrow = listOf("one", "two")
    val providers = mutableListOf<ProvideTokenFn>()

    for (thing in thingsToBorrow) {
        val provider =
            provideTokenFn {
                checkIsStrRef(thing)
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
    @Test
    fun credsAreSendSync() {
        io.github.kotlinmania.awscredentialtypes.tokenfn
            .credsAreSendSync()
    }

    @Test
    fun tokensAreSendSync() {
        io.github.kotlinmania.awscredentialtypes.tokenfn
            .credsAreSendSync()
    }

    @Test
    fun provideTokenFnClosureCanBorrow() =
        runTest {
            io.github.kotlinmania.awscredentialtypes.tokenfn
                .provideTokenFnClosureCanBorrow()
        }
}
