// port-lint: source token_fn.rs
package io.github.kotlinmania.awscredentialtypes.tokenfn

import io.github.kotlinmania.awscredentialtypes.Token
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import io.github.kotlinmania.awscredentialtypes.provider.token.Result as TokenResult

class TokenFnTest {
    @Test
    fun provideTokenFnClosureCanBorrow() =
        runTest {
            fun checkIsString(input: String) {
                assertEquals(input, input)
            }

            suspend fun testAsyncProvider(input: String): TokenResult =
                Result.success(Token.new(input, null))

            val thingsToBorrow = listOf("one", "two")
            val providers = mutableListOf<ProvideTokenFn>()

            for (thing in thingsToBorrow) {
                val provider =
                    provideTokenFn {
                        checkIsString(thing)
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
}
