// port-lint: source credential_fn.rs
package io.github.kotlinmania.awscredentialtypes.credentialfn

import io.github.kotlinmania.awscredentialtypes.Credentials
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CredentialFnTest {
    @Test
    fun provideCredentialsFnClosureCanBorrow() =
        runTest {
            fun checkIsString(input: String) {
                assertEquals(input, input)
            }

            suspend fun testAsyncProvider(input: String): Result<Credentials> =
                Result.success(Credentials.create(input, input, null, null, "test"))

            val thingsToBorrow = listOf("one", "two")
            val providers = mutableListOf<ProvideCredentialsFn>()

            for (thing in thingsToBorrow) {
                val provider =
                    provideCredentialsFn {
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
                    .provideCredentials()
                    .await()
                    .getOrThrow()
                    .accessKeyId(),
            )
            assertEquals(
                "two",
                two
                    .provideCredentials()
                    .await()
                    .getOrThrow()
                    .accessKeyId(),
            )
        }
}
