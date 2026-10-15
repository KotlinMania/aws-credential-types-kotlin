// port-lint: source token_fn.rs
package io.github.kotlinmania.awscredentialtypes.tokenfn

import io.github.kotlinmania.awscredentialtypes.provider.token.ProvideToken
import io.github.kotlinmania.awscredentialtypes.provider.future.ProvideToken as ProvideTokenFuture
import io.github.kotlinmania.awscredentialtypes.provider.token.Result as TokenResult

/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

/**
 * Types that allow an access token provider to be created from a closure
 */

/**
 * A [ProvideToken] implemented by a closure.
 *
 * See [provideTokenFn] for more details (in `aws_credential_types::token_fn`).
 */
class ProvideTokenFn internal constructor(
    private val f: suspend () -> TokenResult,
) : ProvideToken {
    override fun provideToken(): ProvideTokenFuture =
        ProvideTokenFuture.new { f() }

    override fun toString(): String = "ProvideTokenFn"

    /** Formats the provider for debugging, mirroring upstream Debug::fmt. */
    fun fmt(): String = toString()
}

/**
 * Returns a new token provider built with the given closure. This allows you
 * to create an [ProvideToken] implementation from an async block that returns
 * a [TokenResult] (matching `crate::provider::token::Result`).
 *
 * # Examples
 *
 * ```kotlin
 * // use aws_credential_types::Token
 * // use aws_credential_types::token_fn::provide_token_fn
 * suspend fun loadToken(): Token {
 *     return Token.new("token-value", null)
 * }
 *
 * provideTokenFn {
 *     // Async process to retrieve a token goes here
 *     val token = loadToken()
 *     Result.success(token)
 * }
 * ```
 */
fun provideTokenFn(f: suspend () -> TokenResult): ProvideTokenFn =
    ProvideTokenFn(f)
