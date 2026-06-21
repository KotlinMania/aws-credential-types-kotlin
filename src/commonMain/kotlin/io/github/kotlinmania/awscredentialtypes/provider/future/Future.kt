// port-lint: source provider/future.rs
package io.github.kotlinmania.awscredentialtypes.provider.future

import io.github.kotlinmania.awscredentialtypes.Credentials
import io.github.kotlinmania.awscredentialtypes.provider.token.Result as TokenResult

/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

// Convenience holder that wraps either an already-resolved credentials result or a deferred
// producer. Returned from ProvideCredentials.provideCredentials in mirror of the upstream future
// newtype.

private sealed interface CredentialsState {
    data class Ready(
        val value: Result<Credentials>,
    ) : CredentialsState

    class Pending(
        val producer: suspend () -> Result<Credentials>,
    ) : CredentialsState
}

/** Holder that [ProvideCredentials.provideCredentials] must return. */
class ProvideCredentials private constructor(
    private val state: CredentialsState,
) {
    /** Awaits the underlying credentials result. */
    suspend fun await(): Result<Credentials> =
        when (val s = state) {
            is CredentialsState.Ready -> s.value
            is CredentialsState.Pending -> s.producer()
        }

    override fun toString(): String = "ProvideCredentials"

    companion object {
        /** Creates a [ProvideCredentials] holder from a deferred producer. */
        fun new(producer: suspend () -> Result<Credentials>): ProvideCredentials =
            ProvideCredentials(CredentialsState.Pending(producer))

        /** Creates a [ProvideCredentials] holder from a resolved credentials value. */
        fun ready(credentials: Result<Credentials>): ProvideCredentials =
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

/** Holder that [io.github.kotlinmania.awscredentialtypes.provider.token.ProvideToken.provideToken] must return. */
class ProvideToken private constructor(
    private val state: TokenState,
) {
    /** Awaits the underlying token result. */
    suspend fun await(): TokenResult =
        when (val s = state) {
            is TokenState.Ready -> s.value
            is TokenState.Pending -> s.producer()
        }

    override fun toString(): String = "ProvideToken"

    companion object {
        /** Creates a [ProvideToken] holder from a deferred producer. */
        fun new(producer: suspend () -> TokenResult): ProvideToken =
            ProvideToken(TokenState.Pending(producer))

        /** Creates a [ProvideToken] holder from a resolved token value. */
        fun ready(token: TokenResult): ProvideToken =
            ProvideToken(TokenState.Ready(token))
    }
}
