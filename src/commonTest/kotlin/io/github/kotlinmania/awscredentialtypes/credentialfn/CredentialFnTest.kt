// port-lint: tests credential_fn.rs
package io.github.kotlinmania.awscredentialtypes.credentialfn

import io.github.kotlinmania.awscredentialtypes.Credentials
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

private fun <T> assertSendSync() {}

private fun checkIsStrRef(input: String) {
    assertEquals(input, input)
}

private suspend fun testAsyncProvider(input: String): Result<Credentials> =
    Result.success(Credentials.create(input, input, null, null, "test"))

fun credsAreSendSync() {
    assertSendSync<Credentials>()
    val creds = Credentials.forTests()
    assertEquals("ANOTREAL", creds.accessKeyId())
}

suspend fun provideCredentialsFnClosureCanBorrow() {
    val thingsToBorrow = listOf("one", "two")
    val providers = mutableListOf<ProvideCredentialsFn>()

    for (thing in thingsToBorrow) {
        val provider =
            provideCredentialsFn {
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
    @Test
    fun credsAreSendSync() {
        io.github.kotlinmania.awscredentialtypes.credentialfn
            .credsAreSendSync()
    }

    @Test
    fun provideCredentialsFnClosureCanBorrow() =
        runTest {
            io.github.kotlinmania.awscredentialtypes.credentialfn
                .provideCredentialsFnClosureCanBorrow()
        }
}
