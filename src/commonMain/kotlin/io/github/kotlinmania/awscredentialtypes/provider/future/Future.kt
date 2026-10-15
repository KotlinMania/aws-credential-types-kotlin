// port-lint: source provider/future.rs
package io.github.kotlinmania.awscredentialtypes.provider.future

import io.github.kotlinmania.awscredentialtypes.Credentials
import io.github.kotlinmania.awscredentialtypes.provider.token.Result as TokenResult

/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

/**
 * Convenience `ProvideCredentials` struct that implements the `ProvideCredentials` trait.
 */

/** Boxed asynchronous producer mirroring upstream `BoxFuture`. */
typealias BoxFuture<T> = suspend () -> T

/** Output type of [ProvideCredentials], mirroring upstream `Future::Output`. */
typealias Output = Result<Credentials>

/** Output type of [ProvideToken], mirroring upstream `Future::Output`. */
typealias TokenOutput = TokenResult

private sealed interface CredentialsState {
    data class Ready(
        val value: Result<Credentials>,
    ) : CredentialsState

    class Pending(
        val producer: suspend () -> Result<Credentials>,
    ) : CredentialsState
}

/** Future new-type that `ProvideCredentials::provide_credentials` must return. */
class ProvideCredentials private constructor(
    private val state: CredentialsState,
) {
    /** Awaits the underlying credentials result. */
    suspend fun await(): Result<Credentials> =
        when (val s = state) {
            is CredentialsState.Ready -> s.value
            is CredentialsState.Pending -> s.producer()
        }

    /**
     * Polls the underlying credentials result.
     *
     * In Kotlin coroutines, this delegates to [await].
     */
    suspend fun poll(): Result<Credentials> = await()

    override fun toString(): String = "ProvideCredentials"

    companion object {
        /** Creates a `ProvideCredentials` struct from a future. */
        fun new(future: suspend () -> Result<Credentials>): ProvideCredentials =
            ProvideCredentials(CredentialsState.Pending(future))

        /** Creates a `ProvideCredentials` struct from a resolved credentials value. */
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

/** Future new-type that `ProvideToken::provide_token` must return. */
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
