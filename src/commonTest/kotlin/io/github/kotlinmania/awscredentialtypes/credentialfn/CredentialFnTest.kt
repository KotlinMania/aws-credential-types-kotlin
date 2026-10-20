// port-lint: tests credential_fn.rs
package io.github.kotlinmania.awscredentialtypes.credentialfn

import io.github.kotlinmania.awscredentialtypes.Credentials
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

private fun testAsyncProvider(input: String): Result<Credentials> =
    Result.success(Credentials.create(input, input, null, null, "test"))

suspend fun provideCredentialsFnClosureCanBorrow() {
    val thingsToBorrow = listOf("one", "two")
    val providers = mutableListOf<ProvideCredentialsFn>()

    for (thing in thingsToBorrow) {
        val provider =
            provideCredentialsFn {
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

class CredentialFnTest {
    // Upstream Send/Sync type bounds and borrowed-string checks have no Kotlin type-system equivalent.
    @Test
    fun provideCredentialsFnClosureCanBorrow() =
        runTest {
            io.github.kotlinmania.awscredentialtypes.credentialfn
                .provideCredentialsFnClosureCanBorrow()
        }
}
