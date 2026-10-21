// port-lint: source provider/future.rs
package io.github.kotlinmania.awscredentialtypes.provider.future

import io.github.kotlinmania.awscredentialtypes.provider.CredentialsResult
import io.github.kotlinmania.awscredentialtypes.provider.token.TokenResult

/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

private sealed interface CredentialsState {
    data class Ready(
        val value: CredentialsResult,
    ) : CredentialsState

    class Pending(
        val producer: suspend () -> CredentialsResult,
    ) : CredentialsState
}

/** Future returned by a credentials provider. */
class ProvideCredentials private constructor(
    private val state: CredentialsState,
) {
    /** Awaits the underlying credentials result. */
    suspend fun await(): CredentialsResult =
        when (val s = state) {
            is CredentialsState.Ready -> s.value
            is CredentialsState.Pending -> s.producer()
        }

    /**
     * Polls the underlying credentials result.
     *
     * In Kotlin coroutines, this delegates to [await].
     */
    suspend fun poll(): CredentialsResult = await()

    override fun toString(): String = "ProvideCredentials"

    companion object {
        /** Creates a `ProvideCredentials` struct from a future. */
        fun new(future: suspend () -> CredentialsResult): ProvideCredentials =
            ProvideCredentials(CredentialsState.Pending(future))

        /** Creates a `ProvideCredentials` struct from a resolved credentials value. */
        fun ready(credentials: CredentialsResult): ProvideCredentials =
            ProvideCredentials(CredentialsState.Ready(credentials))
    }
}

private sealed interface TokenState {
    data class Ready(
        val value: TokenResult,
    ) : TokenState

    class Pending(
        val producer: suspend () -> TokenResult,
    ) : TokenState
}

/** Future returned by a token provider. */
class ProvideToken private constructor(
    private val state: TokenState,
) {
    /** Awaits the underlying token result. */
    suspend fun await(): TokenResult =
        when (val s = state) {
            is TokenState.Ready -> s.value
            is TokenState.Pending -> s.producer()
        }

    /**
     * Polls the underlying token result.
     *
     * In Kotlin coroutines, this delegates to [await].
     */
    suspend fun poll(): TokenResult = await()

    override fun toString(): String = "ProvideToken"

    companion object {
        /** Creates a `ProvideToken` struct from a future. */
        fun new(future: suspend () -> TokenResult): ProvideToken =
            ProvideToken(TokenState.Pending(future))

        /** Creates a `ProvideToken` struct from a resolved credentials value. */
        fun ready(credentials: TokenResult): ProvideToken =
            ProvideToken(TokenState.Ready(credentials))
    }
}
